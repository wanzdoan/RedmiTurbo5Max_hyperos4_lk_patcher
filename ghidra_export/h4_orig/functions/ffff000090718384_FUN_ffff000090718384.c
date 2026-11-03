// ffff000090718384  size=36  FUN_ffff000090718384

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090718384(void)

{
  long lVar1;
  long lVar2;
  long lVar3;
  
  lVar3 = 0;
  do {
    lVar2 = lVar3 + -0xffff6f7bc908;
    lVar1 = lVar3 + 0x10;
    *(long *)lVar2 = lVar2;
    *(long *)(lVar3 + -0xffff6f7bc900) = lVar2;
    lVar3 = lVar1;
  } while (lVar1 != 0x200);
  _DAT_ffff0000908436b8 = &DAT_ffff0000908436b8;
  _DAT_ffff0000908436c0 = (undefined8 *)&DAT_ffff0000908436b8;
  FUN_ffff00009077ee50(&DAT_ffff0000908438f8,0,0x2d0);
  _DAT_ffff0000908438f8 = 0x74687264;
  FUN_ffff00009077f1e4(0xffff000090843ba8,s_bootstrap_ffff0000907fb334,0x20);
  _DAT_ffff000090843ba0 = 0;
  _DAT_ffff00009084392c = 1;
  _DAT_ffff000090843920 = 0x20000001f;
  _DAT_ffff000090843b88 = 0x77616974;
  _DAT_ffff000090843908 = _DAT_ffff0000908436c0;
  _DAT_ffff000090843b90 = &DAT_ffff000090843b90;
  _DAT_ffff000090843b98 = &DAT_ffff000090843b90;
  _DAT_ffff000090843900 = &DAT_ffff0000908436b8;
  *_DAT_ffff0000908436c0 = &DAT_ffff000090843900;
  _DAT_ffff0000908436c0 = (undefined8 *)&DAT_ffff000090843900;
  tpidr_el1 = 0xffff0000908438f8;
  InstructionSynchronizationBarrier();
  return;
}


