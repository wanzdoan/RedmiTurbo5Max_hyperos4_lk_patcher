// ffff0000907aebc0  size=464  FUN_ffff0000907aebc0

void FUN_ffff0000907aebc0(ulong param_1,int param_2,ulong param_3,long *param_4)

{
  byte bVar1;
  undefined1 uVar2;
  undefined1 uVar3;
  long lVar5;
  int in_w9;
  int in_w10;
  int in_w11;
  int in_w12;
  ulong uVar6;
  undefined1 uVar4;
  
  uVar6 = param_4[2];
  if (CARRY8(uVar6,param_1)) {
    lVar5 = -1;
  }
  else {
    if ((uVar6 <= (ulong)param_4[1]) && (param_1 <= param_4[1] - uVar6)) {
      bVar1 = (byte)param_3;
      if (param_3 < 0x18) {
        *(byte *)(*param_4 + uVar6) = bVar1 | (byte)(param_2 << 5);
      }
      else if (in_w12 == 0) {
        uVar2 = (undefined1)(param_3 >> 8);
        if (in_w9 == 0) {
          uVar3 = (undefined1)(param_3 >> 0x10);
          uVar4 = (undefined1)(param_3 >> 0x18);
          if (in_w10 == 0) {
            if (in_w11 != 0) {
              *(byte *)(*param_4 + uVar6) = (byte)(param_2 << 5) | 0x1b;
              *(char *)(param_4[2] + *param_4 + 1) = (char)(param_3 >> 0x38);
              *(char *)(param_4[2] + *param_4 + 2) = (char)(param_3 >> 0x30);
              *(char *)(param_4[2] + *param_4 + 3) = (char)(param_3 >> 0x28);
              *(char *)(param_4[2] + *param_4 + 4) = (char)(param_3 >> 0x20);
              *(undefined1 *)(param_4[2] + *param_4 + 5) = uVar4;
              *(undefined1 *)(param_4[2] + *param_4 + 6) = uVar3;
              *(undefined1 *)(param_4[2] + *param_4 + 7) = uVar2;
              *(byte *)(param_4[2] + *param_4 + 8) = bVar1;
            }
          }
          else {
            *(byte *)(*param_4 + uVar6) = (byte)(param_2 << 5) | 0x1a;
            *(undefined1 *)(param_4[2] + *param_4 + 1) = uVar4;
            *(undefined1 *)(param_4[2] + *param_4 + 2) = uVar3;
            *(undefined1 *)(param_4[2] + *param_4 + 3) = uVar2;
            *(byte *)(param_4[2] + *param_4 + 4) = bVar1;
          }
        }
        else {
          *(byte *)(*param_4 + uVar6) = (byte)(param_2 << 5) | 0x19;
          *(undefined1 *)(param_4[2] + *param_4 + 1) = uVar2;
          *(byte *)(param_4[2] + *param_4 + 2) = bVar1;
        }
      }
      else {
        *(byte *)(*param_4 + uVar6) = (byte)(param_2 << 5) | 0x18;
        *(byte *)(param_4[2] + *param_4 + 1) = bVar1;
      }
    }
    lVar5 = param_4[2] + param_1;
  }
  param_4[2] = lVar5;
  return;
}


