// ffff0000907a451c  size=756  FUN_ffff0000907a451c

undefined8 FUN_ffff0000907a451c(ulong *param_1,undefined1 *param_2)

{
  ulong *puVar1;
  ulong uVar2;
  ulong uVar3;
  
  if (param_1 == (ulong *)0x0) {
    return 0xfffffffe;
  }
  if (param_2 == (undefined1 *)0x0) {
    return 0xfffffffd;
  }
  uVar3 = param_1[9];
  if (0x3f < uVar3) {
    return 0xfffffffc;
  }
  puVar1 = param_1 + 0xb;
  param_1[9] = uVar3 + 1;
  *param_1 = *param_1 + uVar3 * 8;
  *(undefined1 *)((long)puVar1 + uVar3) = 0x80;
  uVar3 = param_1[9];
  if (uVar3 < 0x39) {
    if (uVar3 == 0x38) goto LAB_ffff0000907a4678;
  }
  else {
    if (uVar3 < 0x40) {
      param_1[9] = uVar3 + 1;
      *(undefined1 *)((long)param_1 + uVar3 + 0x58) = 0;
      if (uVar3 + 1 != 0x40) {
        param_1[9] = uVar3 + 2;
        *(undefined1 *)((long)param_1 + uVar3 + 0x59) = 0;
        if (uVar3 + 2 != 0x40) {
          param_1[9] = uVar3 + 3;
          *(undefined1 *)((long)param_1 + uVar3 + 0x5a) = 0;
          if (uVar3 + 3 != 0x40) {
            param_1[9] = uVar3 + 4;
            *(undefined1 *)((long)param_1 + uVar3 + 0x5b) = 0;
            if (uVar3 + 4 != 0x40) {
              param_1[9] = uVar3 + 5;
              *(undefined1 *)((long)param_1 + uVar3 + 0x5c) = 0;
              if (uVar3 + 5 != 0x40) {
                param_1[9] = uVar3 + 6;
                *(undefined1 *)((long)param_1 + uVar3 + 0x5d) = 0;
                if (uVar3 + 6 != 0x40) {
                  param_1[9] = uVar3 + 7;
                  *(undefined1 *)((long)param_1 + uVar3 + 0x5e) = 0;
                }
              }
            }
          }
        }
      }
    }
    param_1[10] = 0x40;
    FUN_ffff00009079beb4(param_1,puVar1);
    uVar3 = 0;
  }
  do {
    uVar2 = uVar3 + 1;
    param_1[9] = uVar2;
    *(undefined1 *)((long)param_1 + uVar3 + 0x58) = 0;
    uVar3 = uVar2;
  } while (uVar2 != 0x38);
LAB_ffff0000907a4678:
  uVar3 = (*param_1 & 0xff00ff00ff00ff00) >> 8 | (*param_1 & 0xff00ff00ff00ff) << 8;
  uVar3 = (uVar3 & 0xffff0000ffff0000) >> 0x10 | (uVar3 & 0xffff0000ffff) << 0x10;
  param_1[10] = 0x40;
  param_1[0x12] = uVar3 >> 0x20 | uVar3 << 0x20;
  FUN_ffff00009079beb4(param_1,puVar1);
  *param_2 = (char)(param_1[1] >> 0x18);
  param_2[1] = (char)(param_1[1] >> 0x10);
  param_2[2] = (char)(param_1[1] >> 8);
  param_2[3] = (char)param_1[1];
  param_2[4] = (char)(param_1[2] >> 0x18);
  param_2[5] = (char)(param_1[2] >> 0x10);
  param_2[6] = (char)(param_1[2] >> 8);
  param_2[7] = (char)param_1[2];
  param_2[8] = (char)(param_1[3] >> 0x18);
  param_2[9] = (char)(param_1[3] >> 0x10);
  param_2[10] = (char)(param_1[3] >> 8);
  param_2[0xb] = (char)param_1[3];
  param_2[0xc] = (char)(param_1[4] >> 0x18);
  param_2[0xd] = (char)(param_1[4] >> 0x10);
  param_2[0xe] = (char)(param_1[4] >> 8);
  param_2[0xf] = (char)param_1[4];
  param_2[0x10] = (char)(param_1[5] >> 0x18);
  param_2[0x11] = (char)(param_1[5] >> 0x10);
  param_2[0x12] = (char)(param_1[5] >> 8);
  param_2[0x13] = (char)param_1[5];
  param_2[0x14] = (char)(param_1[6] >> 0x18);
  param_2[0x15] = (char)(param_1[6] >> 0x10);
  param_2[0x16] = (char)(param_1[6] >> 8);
  param_2[0x17] = (char)param_1[6];
  param_2[0x18] = (char)(param_1[7] >> 0x18);
  param_2[0x19] = (char)(param_1[7] >> 0x10);
  param_2[0x1a] = (char)(param_1[7] >> 8);
  param_2[0x1b] = (char)param_1[7];
  param_2[0x1c] = (char)(param_1[8] >> 0x18);
  param_2[0x1d] = (char)(param_1[8] >> 0x10);
  param_2[0x1e] = (char)(param_1[8] >> 8);
  param_2[0x1f] = (char)param_1[8];
  return 0;
}


