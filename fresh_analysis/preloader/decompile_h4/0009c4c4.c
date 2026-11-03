// File offset entry: 0x9c4c4

undefined4 FUN_0209c3c4(void)

{
  undefined4 uVar1;
  
  uVar1 = (undefined4)(0x4ffffff030201ff >> (((ulong)uRam000000001800e000 & 7) << 3));
  if (7 < (uRam000000001800e000 & 0xf)) {
    uVar1 = 0xffffffff;
  }
  return uVar1;
}


