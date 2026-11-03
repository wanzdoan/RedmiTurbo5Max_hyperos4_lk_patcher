// File offset entry: 0xa7034

void FUN_020a6f34(long param_1,int *param_2,uint *param_3)

{
  byte *pbVar1;
  byte bVar2;
  uint uVar3;
  long lVar4;
  
  *param_3 = 0;
  *param_2 = 1;
  bVar2 = *(byte *)(param_1 + 1);
  uVar3 = (uint)bVar2;
  if ((char)bVar2 < '\0') {
    *param_2 = (uVar3 & 0x7f) + 1;
    if ((bVar2 & 0x7f) != 0) {
      lVar4 = 0;
      uVar3 = *param_3;
      do {
        pbVar1 = (byte *)(param_1 + 2 + lVar4);
        lVar4 = lVar4 + 1;
        uVar3 = (uint)*pbVar1 | uVar3 << 8;
        *param_3 = uVar3;
      } while ((uint)lVar4 < *param_2 - 1U);
      return;
    }
    uVar3 = (uint)*(byte *)(param_1 + 1);
  }
  *param_3 = uVar3;
  return;
}


