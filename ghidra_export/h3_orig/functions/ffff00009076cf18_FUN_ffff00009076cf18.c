// ffff00009076cf18  size=124  FUN_ffff00009076cf18

ulong FUN_ffff00009076cf18(ulong param_1,int param_2,undefined8 param_3)

{
  long lVar1;
  int iVar2;
  int iVar3;
  
  iVar2 = FUN_ffff00009077ef88(param_3);
  lVar1 = param_1 + (long)param_2;
  while( true ) {
    if ((ulong)(lVar1 - (iVar2 + 1)) < param_1) {
      return 0;
    }
    iVar3 = FUN_ffff00009077e820(param_1,param_3,(long)(iVar2 + 1));
    if (iVar3 == 0) break;
    param_1 = param_1 + 1;
  }
  return param_1;
}


