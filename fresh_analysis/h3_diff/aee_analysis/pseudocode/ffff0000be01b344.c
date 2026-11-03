
void FUN_ffff0000be01b344(void)

{
  long lVar1;
  long lVar2;
  long lVar3;
  
  lVar3 = 0;
  do {
    lVar2 = (long)&DAT_ffff0000be10c730 + lVar3;
    lVar1 = lVar3 + 0x10;
    *(long *)lVar2 = lVar2;
    *(long *)((long)&DAT_ffff0000be10c738 + lVar3) = lVar2;
    lVar3 = lVar1;
  } while (lVar1 != 0x200);
  DAT_ffff0000be10c6f0 = &DAT_ffff0000be10c6f0;
  DAT_ffff0000be10c6f8 = &DAT_ffff0000be10c6f0;
  FUN_ffff0000be060ae0(&DAT_ffff0000be10c930,0,0x2d0);
  DAT_ffff0000be10c930 = 0x74687264;
  FUN_ffff0000be060df0(&DAT_ffff0000be10cbe0,s_bootstrap_ffff0000be0c195b,0x20);
  DAT_ffff0000be10cbd8 = 0;
  DAT_ffff0000be10c964 = 1;
  DAT_ffff0000be10c958 = 0x20000001f;
  DAT_ffff0000be10cbc0 = 0x77616974;
  DAT_ffff0000be10c940 = DAT_ffff0000be10c6f8;
  DAT_ffff0000be10cbc8 = &DAT_ffff0000be10cbc8;
  DAT_ffff0000be10cbd0 = &DAT_ffff0000be10cbc8;
  DAT_ffff0000be10c938 = &DAT_ffff0000be10c6f0;
  *DAT_ffff0000be10c6f8 = &DAT_ffff0000be10c938;
  DAT_ffff0000be10c6f8 = &DAT_ffff0000be10c938;
  tpidr_el1 = 0xffff0000be10c930;
  InstructionSynchronizationBarrier();
  return;
}


