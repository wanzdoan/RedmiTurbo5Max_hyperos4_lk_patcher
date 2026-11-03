
void FUN_ffff0000be018fa8(long param_1)

{
  do {
    do {
    } while (((*(uint *)(param_1 + 0x24) >> 3 ^ *(uint *)(param_1 + 0x24) >> 2) & 1) != 0);
    *(undefined4 *)(param_1 + 0x24) = 0;
  } while ((*(uint *)(param_1 + 0x24) & 1) != 0);
  return;
}


