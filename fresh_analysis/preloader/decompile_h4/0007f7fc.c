// File offset entry: 0x7f7fc

/* WARNING: Removing unreachable block (ram,0x020a3e70) */

undefined8 FUN_0207f6fc(void)

{
  uint uVar1;
  
  if (iRam0000000002100ea8 == -1) {
    iRam0000000002100ea8 = 0x18790314;
  }
  uVar1 = *(uint *)(ulong)uRam0000000002100eac;
  *(uint *)(ulong)uRam0000000002100eac = (uVar1 ^ 0xffffffff) & 0x400000 | uVar1;
  uRam0000000002100eb4 = (uRam0000000002100eb4 ^ 0xffffffff) & 0x400000 | uRam0000000002100eb4;
  uRam0000000002100eb8 = uRam0000000002100eb8 | 0x400000;
  return 0;
}


