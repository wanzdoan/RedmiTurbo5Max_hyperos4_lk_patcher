// ffff0000907160fc  size=44  FUN_ffff0000907160fc

void FUN_ffff0000907160fc(long param_1)

{
  do {
    do {
    } while (((*(uint *)(param_1 + 0x24) >> 3 ^ *(uint *)(param_1 + 0x24) >> 2) & 1) != 0);
    *(undefined4 *)(param_1 + 0x24) = 0;
  } while ((*(uint *)(param_1 + 0x24) & 1) != 0);
  return;
}


