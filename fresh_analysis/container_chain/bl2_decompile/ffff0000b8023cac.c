
undefined8 FUN_ffff0000b8023cac(long *param_1,int *param_2)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  
  *param_2 = 0;
  lVar2 = FUN_ffff0000b804bccc(2000);
  *param_1 = lVar2;
  uVar3 = 0;
  if (lVar2 != 0) {
    iVar1 = FUN_ffff0000b804d74c
                      (lVar2 + *param_2,2000 - (long)*param_2,
                       s_____________________________last_ffff0000b8097229);
    *param_2 = *param_2 + iVar1;
    FUN_ffff0000b8023ae0(&DAT_ffff0000b80e9c98,lVar2,param_2);
    uVar3 = 1;
    if (2000 < *param_2) {
      *param_2 = 2000;
    }
  }
  return uVar3;
}


