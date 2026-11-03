// File offset entry: 0xa3e28

void FUN_020a3d28(ulong param_1,long *param_2)

{
  bool bVar1;
  undefined1 uVar2;
  undefined1 uVar3;
  byte bVar5;
  bool bVar6;
  bool bVar7;
  bool bVar8;
  ulong uVar9;
  long lVar10;
  ulong uVar11;
  undefined1 uVar4;
  
  if (param_1 < 0x18) {
    bVar6 = false;
    bVar7 = false;
    bVar8 = false;
    bVar1 = false;
    uVar9 = 1;
  }
  else if (param_1 < 0x100) {
    bVar7 = false;
    bVar8 = false;
    bVar1 = false;
    bVar6 = true;
    uVar9 = 2;
  }
  else if (param_1 < 0x10000) {
    bVar6 = false;
    bVar8 = false;
    bVar1 = false;
    bVar7 = true;
    uVar9 = 3;
  }
  else {
    bVar8 = param_1 >> 0x20 == 0;
    bVar6 = false;
    bVar7 = false;
    bVar1 = !bVar8;
    uVar9 = 5;
    if (!bVar8) {
      uVar9 = 9;
    }
  }
  uVar11 = param_2[2];
  if (CARRY8(uVar11,uVar9)) {
    lVar10 = -1;
  }
  else {
    if ((uVar11 <= (ulong)param_2[1]) && (uVar9 <= param_2[1] - uVar11)) {
      bVar5 = (byte)param_1;
      if (param_1 < 0x18) {
        *(byte *)(*param_2 + uVar11) = bVar5 | 0xa0;
      }
      else if (bVar6) {
        *(undefined1 *)(*param_2 + uVar11) = 0xb8;
        *(byte *)(param_2[2] + *param_2 + 1) = bVar5;
      }
      else {
        uVar2 = (undefined1)(param_1 >> 8);
        if (bVar7) {
          *(undefined1 *)(*param_2 + uVar11) = 0xb9;
          *(undefined1 *)(param_2[2] + *param_2 + 1) = uVar2;
          *(byte *)(param_2[2] + *param_2 + 2) = bVar5;
        }
        else {
          uVar3 = (undefined1)(param_1 >> 0x18);
          uVar4 = (undefined1)(param_1 >> 0x10);
          if (bVar8) {
            *(undefined1 *)(*param_2 + uVar11) = 0xba;
            *(undefined1 *)(param_2[2] + *param_2 + 1) = uVar3;
            *(undefined1 *)(param_2[2] + *param_2 + 2) = uVar4;
            *(undefined1 *)(param_2[2] + *param_2 + 3) = uVar2;
            *(byte *)(param_2[2] + *param_2 + 4) = bVar5;
          }
          else if (bVar1) {
            *(undefined1 *)(*param_2 + uVar11) = 0xbb;
            *(char *)(param_2[2] + *param_2 + 1) = (char)(param_1 >> 0x38);
            *(char *)(param_2[2] + *param_2 + 2) = (char)(param_1 >> 0x30);
            *(char *)(param_2[2] + *param_2 + 3) = (char)(param_1 >> 0x28);
            *(char *)(param_2[2] + *param_2 + 4) = (char)(param_1 >> 0x20);
            *(undefined1 *)(param_2[2] + *param_2 + 5) = uVar3;
            *(undefined1 *)(param_2[2] + *param_2 + 6) = uVar4;
            *(undefined1 *)(param_2[2] + *param_2 + 7) = uVar2;
            *(byte *)(param_2[2] + *param_2 + 8) = bVar5;
          }
        }
      }
    }
    lVar10 = param_2[2] + uVar9;
  }
  param_2[2] = lVar10;
  return;
}


