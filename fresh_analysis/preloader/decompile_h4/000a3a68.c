// File offset entry: 0xa3a68

void FUN_020a3968(ulong param_1,long *param_2)

{
  bool bVar1;
  bool bVar2;
  undefined1 uVar3;
  undefined1 uVar4;
  byte bVar6;
  bool bVar7;
  bool bVar8;
  bool bVar9;
  ulong uVar10;
  long lVar11;
  ulong uVar12;
  undefined1 uVar5;
  
  bVar2 = (long)param_1 < 0;
  if (bVar2) {
    param_1 = ~param_1;
  }
  if (param_1 < 0x18) {
    bVar7 = false;
    bVar8 = false;
    bVar9 = false;
    bVar1 = false;
    uVar10 = 1;
  }
  else if (param_1 < 0x100) {
    bVar8 = false;
    bVar9 = false;
    bVar1 = false;
    bVar7 = true;
    uVar10 = 2;
  }
  else if (param_1 < 0x10000) {
    bVar7 = false;
    bVar9 = false;
    bVar1 = false;
    bVar8 = true;
    uVar10 = 3;
  }
  else {
    bVar9 = param_1 >> 0x20 == 0;
    bVar7 = false;
    bVar8 = false;
    bVar1 = !bVar9;
    uVar10 = 5;
    if (!bVar9) {
      uVar10 = 9;
    }
  }
  uVar12 = param_2[2];
  if (CARRY8(uVar12,uVar10)) {
    lVar11 = -1;
  }
  else {
    if ((uVar12 <= (ulong)param_2[1]) && (uVar10 <= param_2[1] - uVar12)) {
      bVar6 = (byte)param_1;
      if (param_1 < 0x18) {
        *(byte *)(*param_2 + uVar12) = bVar6 | bVar2 << 5;
      }
      else if (bVar7) {
        *(byte *)(*param_2 + uVar12) = bVar2 << 5 | 0x18;
        *(byte *)(param_2[2] + *param_2 + 1) = bVar6;
      }
      else {
        uVar3 = (undefined1)(param_1 >> 8);
        if (bVar8) {
          *(byte *)(*param_2 + uVar12) = bVar2 << 5 | 0x19;
          *(undefined1 *)(param_2[2] + *param_2 + 1) = uVar3;
          *(byte *)(param_2[2] + *param_2 + 2) = bVar6;
        }
        else {
          uVar4 = (undefined1)(param_1 >> 0x18);
          uVar5 = (undefined1)(param_1 >> 0x10);
          if (bVar9) {
            *(byte *)(*param_2 + uVar12) = bVar2 << 5 | 0x1a;
            *(undefined1 *)(param_2[2] + *param_2 + 1) = uVar4;
            *(undefined1 *)(param_2[2] + *param_2 + 2) = uVar5;
            *(undefined1 *)(param_2[2] + *param_2 + 3) = uVar3;
            *(byte *)(param_2[2] + *param_2 + 4) = bVar6;
          }
          else if (bVar1) {
            *(byte *)(*param_2 + uVar12) = bVar2 << 5 | 0x1b;
            *(char *)(param_2[2] + *param_2 + 1) = (char)(param_1 >> 0x38);
            *(char *)(param_2[2] + *param_2 + 2) = (char)(param_1 >> 0x30);
            *(char *)(param_2[2] + *param_2 + 3) = (char)(param_1 >> 0x28);
            *(char *)(param_2[2] + *param_2 + 4) = (char)(param_1 >> 0x20);
            *(undefined1 *)(param_2[2] + *param_2 + 5) = uVar4;
            *(undefined1 *)(param_2[2] + *param_2 + 6) = uVar5;
            *(undefined1 *)(param_2[2] + *param_2 + 7) = uVar3;
            *(byte *)(param_2[2] + *param_2 + 8) = bVar6;
          }
        }
      }
    }
    lVar11 = param_2[2] + uVar10;
  }
  param_2[2] = lVar11;
  return;
}


