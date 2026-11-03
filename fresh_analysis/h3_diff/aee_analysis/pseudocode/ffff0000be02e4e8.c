
undefined8 FUN_ffff0000be02e4e8(long *param_1,int *param_2)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  
  *param_2 = 0;
  lVar2 = FUN_ffff0000be05f1bc(0x2000);
  *param_1 = lVar2;
  uVar3 = 0;
  if (lVar2 != 0) {
    iVar1 = FUN_ffff0000be0611dc
                      (lVar2 + *param_2,0x2000 - (long)*param_2,
                       s_____________________________retr_ffff0000be0b4133);
    *param_2 = *param_2 + iVar1;
    iVar1 = FUN_ffff0000be02c5ac();
    if (iVar1 == 0) {
      iVar1 = FUN_ffff0000be0611dc
                        (lVar2 + *param_2,0x2000 - (long)*param_2,s_NO_DFD_trigger_ffff0000be0afaf8)
      ;
    }
    else {
      iVar1 = FUN_ffff0000be02c108();
    }
    iVar1 = *param_2 + iVar1;
    uVar3 = 1;
    if (0x1fff < iVar1) {
      iVar1 = 0x2000;
    }
    *param_2 = iVar1;
  }
  return uVar3;
}


