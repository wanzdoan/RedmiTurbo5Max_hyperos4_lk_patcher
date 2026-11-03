
void FUN_ffff0000b800b9a4(void)

{
  long lVar1;
  long lVar2;
  long lVar3;
  
  lVar3 = 0;
  do {
    lVar2 = (long)&DAT_ffff0000b810ff70 + lVar3;
    lVar1 = lVar3 + 0x10;
    *(long *)lVar2 = lVar2;
    *(long *)((long)&DAT_ffff0000b810ff78 + lVar3) = lVar2;
    lVar3 = lVar1;
  } while (lVar1 != 0x200);
  DAT_ffff0000b810ff30 = &DAT_ffff0000b810ff30;
  DAT_ffff0000b810ff38 = &DAT_ffff0000b810ff30;
  FUN_ffff0000b804d070(&DAT_ffff0000b8110170,0,0x2d0);
  DAT_ffff0000b8110170 = 0x74687264;
  FUN_ffff0000b804d360(&DAT_ffff0000b8110420,s_bootstrap_ffff0000b809d3c4,0x20);
  DAT_ffff0000b8110418 = 0;
  DAT_ffff0000b81101a4 = 1;
  DAT_ffff0000b8110198 = 0x20000001f;
  DAT_ffff0000b8110400 = 0x77616974;
  DAT_ffff0000b8110180 = DAT_ffff0000b810ff38;
  DAT_ffff0000b8110408 = &DAT_ffff0000b8110408;
  DAT_ffff0000b8110410 = &DAT_ffff0000b8110408;
  DAT_ffff0000b8110178 = &DAT_ffff0000b810ff30;
  *DAT_ffff0000b810ff38 = &DAT_ffff0000b8110178;
  DAT_ffff0000b810ff38 = &DAT_ffff0000b8110178;
  tpidr_el1 = 0xffff0000b8110170;
  InstructionSynchronizationBarrier();
  return;
}


