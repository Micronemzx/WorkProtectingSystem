#include <string>
#include <cstring>
#include <cmath>
#include <iostream>
#include <cstdio>
#include <cstdlib>
#include <algorithm>
using namespace std;
int len1,len2,ans,f[1000005],net[1000005],c[1000005];
string st,sr;
int main()
{
	cin>>st;
	cin>>sr;
	len1=st.size();
	len2=sr.size();
	st=' '+st;
	sr=' '+sr;
	net[1]=0;
	for (int i=2,j=0;i<=len2;i++)
	 {
		while (j>0&&sr[i]!=sr[j+1]) j=net[j];
		if (sr[i]==sr[j+1]) j++;
		net[i]=j;
}
	for (int i=1;i<=len2;++i) printf("%d ",net[i]);
    for (int i=1,j=0;i<=len1;i++)
     {
     	while (j>0&&(j==len2||st[i]!=sr[j+1])) j=net[j];
		if (st[i]==sr[j+1]) j++;
		f[i]=j;
		if (f[i]==len2) { ans++; c[ans]=i-len2+1; }
     }
    for (int i=1;i<=ans;i++)
     cout<<c[i]<<endl; 
    /*for (int i=1;i<=len2;i++)
     printf("%d ",net[i]);*/
	return 0;
}
