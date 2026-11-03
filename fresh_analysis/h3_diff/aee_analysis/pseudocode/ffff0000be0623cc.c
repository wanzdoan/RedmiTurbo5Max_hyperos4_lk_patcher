
void FUN_ffff0000be0623cc(long param_1,ulong param_2,uint param_3,byte *param_4)

{
  bool bVar1;
  bool bVar2;
  byte bVar3;
  ulong uVar4;
  long lVar5;
  ulong uVar6;
  
  bVar2 = (param_3 & 0x400) != 0;
  lVar5 = 0x1e;
  *(undefined1 *)(param_1 + 0x1f) = 0;
  uVar4 = -param_2;
  if (!bVar2 || (long)param_2 >= 0) {
    uVar4 = param_2;
  }
  uVar6 = uVar4;
  if (9 < uVar4) {
    do {
      uVar4 = uVar6 / 10;
      *(byte *)(param_1 + lVar5) = (char)uVar6 + (char)uVar4 * -10 | 0x30;
      lVar5 = lVar5 + -1;
      bVar1 = 99 < uVar6;
      uVar6 = uVar4;
    } while (bVar1);
  }
  *(char *)(param_1 + lVar5) = (char)uVar4 + '0';
  if (bVar2 && (long)param_2 < 0) {
    bVar3 = 0x2d;
  }
  else if ((param_3 >> 9 & 1) == 0) {
    bVar3 = (byte)(param_3 >> 8) & 0x20;
  }
  else {
    bVar3 = 0x2b;
  }
  *param_4 = bVar3;
  return;
}


