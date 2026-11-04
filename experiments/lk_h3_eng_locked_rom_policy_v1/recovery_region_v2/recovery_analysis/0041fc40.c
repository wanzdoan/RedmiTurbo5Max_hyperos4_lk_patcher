
/* WARNING: Unknown calling convention -- yet parameter storage is locked */

char * strerror(int __errnum)

{
  char *pcVar1;

  pcVar1 = (char *)(*(code *)PTR_strerror_0042a5a8)(__errnum);
  return pcVar1;
}
