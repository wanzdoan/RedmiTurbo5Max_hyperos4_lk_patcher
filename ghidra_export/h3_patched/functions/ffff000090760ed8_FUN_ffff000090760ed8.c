// ffff000090760ed8  size=80  FUN_ffff000090760ed8

void FUN_ffff000090760ed8(undefined8 *param_1)

{
  if (param_1 == (undefined8 *)0x0) {
    FUN_ffff00009077de54
              (s__s__d____s___log_exception_shutd_ffff0000907d4da5,
               s_InitExceptionShutdownlogHeader_ffff0000907ecd1e,0x26a,s_BLACKBOX_ffff0000907d9988);
    return;
  }
  if (DAT_ffff00009087bf14 == '\x01') {
    FUN_ffff00009077de54
              (s__s__d__blackbox_v1__no_exception_ffff0000907f6d45,
               s_InitExceptionShutdownlogHeader_ffff0000907ecd1e,0x26e);
    return;
  }
  *(undefined4 *)(param_1 + 1) = 0;
  *param_1 = 0xa7e2c4b6d8f0a1d3;
  *(undefined8 *)((long)param_1 + 0xc) = 0xac10c00;
  *(undefined8 *)((long)param_1 + 0x14) = 0x40000000000;
  *(undefined8 *)((long)param_1 + 0x1c) = 0x10000;
  FUN_ffff00009077eb88((long)param_1 + 0x24,0,0x28);
  FUN_ffff00009077eb88((long)param_1 + 0x4c,0,0x28);
  FUN_ffff00009077eb88((long)param_1 + 0x74,0,0x28);
  FUN_ffff00009077eb88((long)param_1 + 0x9c,0,0xf0);
  return;
}


