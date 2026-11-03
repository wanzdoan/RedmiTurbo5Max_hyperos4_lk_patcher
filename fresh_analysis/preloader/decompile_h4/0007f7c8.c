// File offset entry: 0x7f7c8

/* WARNING: Possible PIC construction at 0x0207f6e0: Changing call to branch */
/* WARNING: Removing unreachable block (ram,0x0207f6e4) */
/* WARNING: Removing unreachable block (ram,0x020a3e70) */

undefined8 FUN_0207f6c8(void)

{
  uint uVar1;
  
  if (iRam0000000002100e6c == -1) {
    iRam0000000002100e6c = 0x18790314;
  }
  uVar1 = *(uint *)(ulong)uRam0000000002100e70;
  *(uint *)(ulong)uRam0000000002100e70 = (uVar1 ^ 0xffffffff) & 0x40000 | uVar1;
  uRam0000000002100e78 = (uRam0000000002100e78 ^ 0xffffffff) & 0x40000 | uRam0000000002100e78;
  uRam0000000002100e7c = uRam0000000002100e7c | 0x40000;
  return 0;
}


