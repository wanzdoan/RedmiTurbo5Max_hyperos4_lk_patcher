
undefined8 FUN_ffff0000b8024104(long *param_1,int *param_2)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  
  *param_2 = 0;
  lVar2 = FUN_ffff0000b804bccc(0x2000);
  *param_1 = lVar2;
  uVar3 = 0;
  if (lVar2 != 0) {
    iVar1 = FUN_ffff0000b804d74c
                      (lVar2 + *param_2,0x2000 - (long)*param_2,
                       s_____________________________retr_ffff0000b8092671);
    *param_2 = *param_2 + iVar1;
    iVar1 = FUN_ffff0000b8022a1c();
    if (iVar1 == 0) {
      iVar1 = FUN_ffff0000b804d74c
                        (lVar2 + *param_2,0x2000 - (long)*param_2,s_NO_DFD_trigger_ffff0000b808edea)
      ;
    }
    else {
      iVar1 = FUN_ffff0000b8022578();
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


