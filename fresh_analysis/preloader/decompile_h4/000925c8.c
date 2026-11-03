// File offset entry: 0x925c8

void FUN_020924c8(uint param_1)

{
  do {
  } while ((*(uint *)(ulong)(uRam0000000002100888 + 0x14) >> 5 & 1) == 0);
  if ((param_1 & 0xff) == 10) {
    *(undefined4 *)(ulong)uRam0000000002100888 = 0xd;
  }
  *(uint *)(ulong)uRam0000000002100888 = param_1 & 0xff;
  return;
}


