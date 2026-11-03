
void FUN_ffff0000be088ff4(int param_1,ulong param_2,long *param_3)

{
  bool bVar1;
  byte bVar2;
  undefined1 uVar3;
  undefined1 uVar4;
  bool bVar6;
  bool bVar7;
  bool bVar8;
  ulong uVar9;
  long lVar10;
  ulong uVar11;
  undefined1 uVar5;
  
  if (param_2 < 0x18) {
    bVar6 = false;
    bVar7 = false;
    bVar8 = false;
    bVar1 = false;
    uVar9 = 1;
  }
  else if (param_2 < 0x100) {
    bVar7 = false;
    bVar8 = false;
    bVar1 = false;
    bVar6 = true;
    uVar9 = 2;
  }
  else if (param_2 < 0x10000) {
    bVar6 = false;
    bVar8 = false;
    bVar1 = false;
    bVar7 = true;
    uVar9 = 3;
  }
  else {
    bVar8 = (int)(param_2 >> 0x20) == 0;
    bVar6 = false;
    bVar7 = false;
    bVar1 = !bVar8;
    uVar9 = 5;
    if (!bVar8) {
      uVar9 = 9;
    }
  }
  uVar11 = param_3[2];
  if (CARRY8(uVar11,uVar9)) {
    lVar10 = -1;
  }
  else {
    if ((uVar11 <= (ulong)param_3[1]) && (uVar9 <= param_3[1] - uVar11)) {
      bVar2 = (byte)param_2;
      if (param_2 < 0x18) {
        *(byte *)(*param_3 + uVar11) = bVar2 | (byte)(param_1 << 5);
      }
      else if (bVar6) {
        *(byte *)(*param_3 + uVar11) = (byte)(param_1 << 5) | 0x18;
        *(byte *)(param_3[2] + *param_3 + 1) = bVar2;
      }
      else {
        uVar3 = (undefined1)(param_2 >> 8);
        if (bVar7) {
          *(byte *)(*param_3 + uVar11) = (byte)(param_1 << 5) | 0x19;
          *(undefined1 *)(param_3[2] + *param_3 + 1) = uVar3;
          *(byte *)(param_3[2] + *param_3 + 2) = bVar2;
        }
        else {
          uVar4 = (undefined1)(param_2 >> 0x10);
          uVar5 = (undefined1)(param_2 >> 0x18);
          if (bVar8) {
            *(byte *)(*param_3 + uVar11) = (byte)(param_1 << 5) | 0x1a;
            *(undefined1 *)(param_3[2] + *param_3 + 1) = uVar5;
            *(undefined1 *)(param_3[2] + *param_3 + 2) = uVar4;
            *(undefined1 *)(param_3[2] + *param_3 + 3) = uVar3;
            *(byte *)(param_3[2] + *param_3 + 4) = bVar2;
          }
          else if (bVar1) {
            *(byte *)(*param_3 + uVar11) = (byte)(param_1 << 5) | 0x1b;
            *(char *)(param_3[2] + *param_3 + 1) = (char)(param_2 >> 0x38);
            *(char *)(param_3[2] + *param_3 + 2) = (char)(param_2 >> 0x30);
            *(char *)(param_3[2] + *param_3 + 3) = (char)(param_2 >> 0x28);
            *(char *)(param_3[2] + *param_3 + 4) = (char)(param_2 >> 0x20);
            *(undefined1 *)(param_3[2] + *param_3 + 5) = uVar5;
            *(undefined1 *)(param_3[2] + *param_3 + 6) = uVar4;
            *(undefined1 *)(param_3[2] + *param_3 + 7) = uVar3;
            *(byte *)(param_3[2] + *param_3 + 8) = bVar2;
          }
        }
      }
    }
    lVar10 = param_3[2] + uVar9;
  }
  param_3[2] = lVar10;
  return;
}


