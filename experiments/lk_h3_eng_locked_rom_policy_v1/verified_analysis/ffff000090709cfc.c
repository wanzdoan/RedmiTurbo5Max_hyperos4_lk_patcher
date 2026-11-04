
void FUN_ffff000090709cfc(long param_1)

{
  long lVar1;
  undefined8 uVar2;
  code *pcVar3;

  if ((param_1 != 0) && (DAT_ffff000090835158 == 1)) {
    lVar1 = FUN_ffff00009077dec4(0x40,0x40);
    if (lVar1 != 0) {
      FUN_ffff00009077ffe0(lVar1,0x40,s_INFO_s_ffff0000907fadeb,param_1);
      pcVar3 = *(code **)(DAT_ffff000090835170 + 0x30);
      uVar2 = FUN_ffff00009077fb34(lVar1);
      (*pcVar3)(lVar1,uVar2,5000);
      thunk_FUN_ffff0000907c310c(lVar1);
      return;
    }
    FUN_ffff00009077ea84(s_fastboot__can_t_allocate_memory_ffff0000907f91d6);
    return;
  }
  return;
}
