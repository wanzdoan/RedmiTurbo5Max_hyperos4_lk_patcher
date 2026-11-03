
undefined8 FUN_ffff0000be02e090(long *param_1,int *param_2)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  
  *param_2 = 0;
  lVar2 = FUN_ffff0000be05f1bc(2000);
  *param_1 = lVar2;
  uVar3 = 0;
  if (lVar2 != 0) {
    iVar1 = FUN_ffff0000be0611dc
                      (lVar2 + *param_2,2000 - (long)*param_2,
                       s_____________________________last_ffff0000be0b9d23);
    *param_2 = *param_2 + iVar1;
    FUN_ffff0000be02dec4(&DAT_ffff0000be0d72a0,lVar2,param_2);
    uVar3 = 1;
    if (2000 < *param_2) {
      *param_2 = 2000;
    }
  }
  return uVar3;
}


