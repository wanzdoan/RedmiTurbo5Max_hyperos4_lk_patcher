// ffff000090709c60  size=176  FUN_ffff000090709c60

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090709c60(long param_1)

{
  long lVar1;
  undefined8 uVar2;
  code *pcVar3;
  
  if ((param_1 != 0) && (_DAT_ffff00009083d158 == 1)) {
    lVar1 = FUN_ffff00009077d294(0x40,0x40);
    if (lVar1 != 0) {
      FUN_ffff00009077f434(lVar1,0x40,s_INFO_s_ffff000090801cd1,param_1);
      pcVar3 = *(code **)(_DAT_ffff00009083d170 + 0x30);
      uVar2 = FUN_ffff00009077ef88(lVar1);
      (*pcVar3)(lVar1,uVar2,5000);
      thunk_FUN_ffff0000907c2ad8(lVar1);
      return;
    }
    FUN_ffff00009077de54(s_fastboot__can_t_allocate_memory_ffff0000908001d1);
    return;
  }
  return;
}


