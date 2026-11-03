// File offset entry: 0x91764

void FUN_02091664(byte *param_1,byte param_2,ulong param_3)

{
  ulong uVar1;
  byte *pbVar2;
  ulong uVar3;
  
  uVar3 = (ulong)(uint)-(int)param_1 & 7;
  uVar1 = param_3 - uVar3;
  if (uVar3 <= param_3 && uVar1 != 0) {
    pbVar2 = param_1;
    if ((-(int)param_1 & 7U) != 0) {
      do {
        param_1 = pbVar2 + 1;
        *pbVar2 = param_2;
        uVar3 = uVar3 - 1;
        pbVar2 = param_1;
      } while (uVar3 != 0);
    }
    for (uVar3 = uVar1 >> 3; uVar3 != 0; uVar3 = uVar3 - 1) {
      *(ulong *)param_1 = CONCAT44((uint)param_2 * 0x1010101,(uint)param_2 * 0x1010101);
      param_1 = param_1 + 8;
    }
    param_3 = uVar1 & 7;
  }
  for (; param_3 != 0; param_3 = param_3 - 1) {
    *param_1 = param_2;
    param_1 = param_1 + 1;
  }
  return;
}


