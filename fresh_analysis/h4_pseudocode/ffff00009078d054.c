
void FUN_ffff00009078d054(long param_1)

{
  long lVar1;
  long lVar2;
  
  lVar2 = (ulong)*(byte *)(param_1 + 0x40) * 0x10;
  lVar1 = -0xffffe98fea00;
  if (*(char *)(param_1 + 0x41) != '\0') {
    lVar1 = -0xffffe98feb00;
  }
  *(undefined4 *)(lVar1 + lVar2) = 2;
  if ((*(uint *)(lVar1 + lVar2) >> 0xf & 1) == 0) {
    *(undefined4 *)(lVar1 + lVar2) = 2;
  }
  return;
}


