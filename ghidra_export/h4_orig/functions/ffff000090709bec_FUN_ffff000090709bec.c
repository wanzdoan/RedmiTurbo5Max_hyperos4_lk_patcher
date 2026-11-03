// ffff000090709bec  size=176  FUN_ffff000090709bec

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090709bec(long param_1)

{
  long lVar1;
  undefined8 uVar2;
  code *pcVar3;
  
  if ((param_1 != 0) && (_DAT_ffff00009083d0f8 == 1)) {
    lVar1 = FUN_ffff00009077d55c(0x40,0x40);
    if (lVar1 != 0) {
      FUN_ffff00009077f6fc(lVar1,0x40,s_INFO_s_ffff000090801ead,param_1);
      pcVar3 = *(code **)(_DAT_ffff00009083d110 + 0x30);
      uVar2 = FUN_ffff00009077f250(lVar1);
      (*pcVar3)(lVar1,uVar2,5000);
      thunk_FUN_ffff0000907c314c(lVar1);
      return;
    }
    FUN_ffff00009077e11c(s_fastboot__can_t_allocate_memory_ffff000090800379);
    return;
  }
  return;
}


