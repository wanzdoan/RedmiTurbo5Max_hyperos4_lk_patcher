
undefined8 FUN_ffff00009079f598(long param_1,uint param_2)

{
  int iVar1;
  uint uVar2;
  long lVar3;
  undefined8 uVar4;
  
  if (*(uint *)(param_1 + 4) < param_2) {
    uVar2 = 0;
    if (DAT_ffff000090837c78 != 0) {
      uVar2 = ((param_2 + DAT_ffff000090837c78) - 1) / DAT_ffff000090837c78;
    }
    iVar1 = uVar2 * DAT_ffff000090837c78;
    lVar3 = FUN_ffff00009077d5a8(iVar1,2);
    if (lVar3 == 0) {
      uVar4 = 0xfffffffe;
    }
    else {
      FUN_ffff00009077eb20(lVar3,*(undefined8 *)(param_1 + 0x10),(ulong)*(uint *)(param_1 + 8) << 1)
      ;
      FUN_ffff00009077ee50(*(undefined8 *)(param_1 + 0x10),0,(ulong)*(uint *)(param_1 + 4) << 1);
      if (*(long *)(param_1 + 0x10) != 0) {
        thunk_FUN_ffff0000907c314c();
      }
      uVar4 = 0;
      *(long *)(param_1 + 0x10) = lVar3;
      *(int *)(param_1 + 4) = iVar1;
    }
  }
  else {
    uVar4 = 0;
  }
  return uVar4;
}


