// ffff000090709b88  size=216  FUN_ffff000090709b88

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090709b88(undefined8 param_1,undefined *param_2)

{
  undefined *puVar1;
  long lVar2;
  undefined8 uVar3;
  code *pcVar4;
  
  if (_DAT_ffff00009083d158 != 1) {
    return;
  }
  lVar2 = FUN_ffff00009077d294(0x40,0x40);
  if (lVar2 != 0) {
    puVar1 = &DAT_ffff0000907df295;
    if (param_2 != (undefined *)0x0) {
      puVar1 = param_2;
    }
    FUN_ffff00009077f434(lVar2,0x40,&DAT_ffff0000907e097c,param_1,puVar1);
    _DAT_ffff00009083d158 = 2;
    pcVar4 = *(code **)(_DAT_ffff00009083d170 + 0x30);
    uVar3 = FUN_ffff00009077ef88(lVar2);
    (*pcVar4)(lVar2,uVar3,5000);
    thunk_FUN_ffff0000907c2ad8(lVar2);
    return;
  }
  FUN_ffff00009077de54(s_fastboot__can_t_allocate_memory_ffff0000908001d1);
  return;
}


