// ffff000090718240  size=36  FUN_ffff000090718240

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090718240(void)

{
  long lVar1;
  long lVar2;
  long lVar3;
  
  lVar3 = 0;
  do {
    lVar2 = lVar3 + -0xffff6f7bc940;
    lVar1 = lVar3 + 0x10;
    *(long *)lVar2 = lVar2;
    *(long *)(lVar3 + -0xffff6f7bc938) = lVar2;
    lVar3 = lVar1;
  } while (lVar1 != 0x200);
  _DAT_ffff000090843680 = &DAT_ffff000090843680;
  _DAT_ffff000090843688 = (undefined8 *)&DAT_ffff000090843680;
  FUN_ffff00009077eb88(&DAT_ffff0000908438c0,0,0x2d0);
  _DAT_ffff0000908438c0 = 0x74687264;
  FUN_ffff00009077ef1c(0xffff000090843b70,s_bootstrap_ffff0000907fb198,0x20);
  _DAT_ffff000090843b68 = 0;
  _DAT_ffff0000908438f4 = 1;
  _DAT_ffff0000908438e8 = 0x20000001f;
  _DAT_ffff000090843b50 = 0x77616974;
  _DAT_ffff0000908438d0 = _DAT_ffff000090843688;
  _DAT_ffff000090843b58 = &DAT_ffff000090843b58;
  _DAT_ffff000090843b60 = &DAT_ffff000090843b58;
  _DAT_ffff0000908438c8 = &DAT_ffff000090843680;
  *_DAT_ffff000090843688 = &DAT_ffff0000908438c8;
  _DAT_ffff000090843688 = (undefined8 *)&DAT_ffff0000908438c8;
  tpidr_el1 = 0xffff0000908438c0;
  InstructionSynchronizationBarrier();
  return;
}


