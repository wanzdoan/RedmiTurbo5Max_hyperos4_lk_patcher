// ffff00009073e21c  size=184  FUN_ffff00009073e21c

undefined8 FUN_ffff00009073e21c(long *param_1,int *param_2)

{
  int iVar1;
  long lVar2;
  undefined8 uVar3;
  
  *param_2 = 0;
  lVar2 = FUN_ffff00009077d514(0x2000);
  *param_1 = lVar2;
  uVar3 = 0;
  if (lVar2 != 0) {
    iVar1 = FUN_ffff00009077f6fc
                      (lVar2 + *param_2,0x2000 - (long)*param_2,
                       s_____________________________retr_ffff0000907e7b8d);
    *param_2 = *param_2 + iVar1;
    iVar1 = FUN_ffff00009073bfec();
    if (iVar1 == 0) {
      iVar1 = FUN_ffff00009077f6fc
                        (lVar2 + *param_2,0x2000 - (long)*param_2,s_NO_DFD_trigger_ffff0000907e15b8)
      ;
    }
    else {
      iVar1 = FUN_ffff00009073bb48();
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


