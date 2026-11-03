// ffff0000907a533c  size=772  FUN_ffff0000907a533c

undefined8 FUN_ffff0000907a533c(ulong *param_1,undefined1 *param_2)

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
  uVar2 = param_1[10];
  if (0x7f < uVar2) {
    return 0xfffffffc;
  }
  uVar3 = *param_1;
  *param_1 = uVar3 + uVar2 * 8;
  if (CARRY8(uVar3,uVar2 * 8)) {
    param_1[1] = param_1[1] + 1;
  }
  puVar1 = param_1 + 0xc;
  param_1[10] = uVar2 + 1;
  *(undefined1 *)((long)puVar1 + uVar2) = 0x80;
  uVar2 = param_1[10];
  if (uVar2 < 0x71) {
    if (uVar2 == 0x70) goto LAB_ffff0000907a5424;
  }
  else {
    if (uVar2 < 0x80) {
      do {
        uVar3 = uVar2 + 1;
        param_1[10] = uVar3;
        *(undefined1 *)((long)param_1 + uVar2 + 0x60) = 0;
        uVar2 = uVar3;
      } while (uVar3 != 0x80);
    }
    param_1[0xb] = 0x80;
    FUN_ffff0000907a5164(param_1,puVar1);
    uVar2 = 0;
  }
  do {
    uVar3 = uVar2 + 1;
    param_1[10] = uVar3;
    *(undefined1 *)((long)param_1 + uVar2 + 0x60) = 0;
    uVar2 = uVar3;
  } while (uVar3 != 0x70);
LAB_ffff0000907a5424:
  uVar2 = (*param_1 & 0xff00ff00ff00ff00) >> 8 | (*param_1 & 0xff00ff00ff00ff) << 8;
  uVar2 = (uVar2 & 0xffff0000ffff0000) >> 0x10 | (uVar2 & 0xffff0000ffff) << 0x10;
  param_1[0xb] = 0x80;
  uVar3 = (param_1[1] & 0xff00ff00ff00ff00) >> 8 | (param_1[1] & 0xff00ff00ff00ff) << 8;
  uVar3 = (uVar3 & 0xffff0000ffff0000) >> 0x10 | (uVar3 & 0xffff0000ffff) << 0x10;
  param_1[0x1a] = uVar3 >> 0x20 | uVar3 << 0x20;
  param_1[0x1b] = uVar2 >> 0x20 | uVar2 << 0x20;
  FUN_ffff0000907a5164(param_1,puVar1);
  *param_2 = *(undefined1 *)((long)param_1 + 0x17);
  param_2[1] = (char)*(undefined2 *)((long)param_1 + 0x16);
  param_2[2] = (char)(param_1[2] >> 0x28);
  param_2[3] = (char)*(undefined4 *)((long)param_1 + 0x14);
  param_2[4] = (char)(param_1[2] >> 0x18);
  param_2[5] = (char)(param_1[2] >> 0x10);
  param_2[6] = (char)(param_1[2] >> 8);
  param_2[7] = (char)param_1[2];
  param_2[8] = *(undefined1 *)((long)param_1 + 0x1f);
  param_2[9] = (char)*(undefined2 *)((long)param_1 + 0x1e);
  param_2[10] = (char)(param_1[3] >> 0x28);
  param_2[0xb] = (char)*(undefined4 *)((long)param_1 + 0x1c);
  param_2[0xc] = (char)(param_1[3] >> 0x18);
  param_2[0xd] = (char)(param_1[3] >> 0x10);
  param_2[0xe] = (char)(param_1[3] >> 8);
  param_2[0xf] = (char)param_1[3];
  param_2[0x10] = *(undefined1 *)((long)param_1 + 0x27);
  param_2[0x11] = (char)*(undefined2 *)((long)param_1 + 0x26);
  param_2[0x12] = (char)(param_1[4] >> 0x28);
  param_2[0x13] = (char)*(undefined4 *)((long)param_1 + 0x24);
  param_2[0x14] = (char)(param_1[4] >> 0x18);
  param_2[0x15] = (char)(param_1[4] >> 0x10);
  param_2[0x16] = (char)(param_1[4] >> 8);
  param_2[0x17] = (char)param_1[4];
  param_2[0x18] = *(undefined1 *)((long)param_1 + 0x2f);
  param_2[0x19] = (char)*(undefined2 *)((long)param_1 + 0x2e);
  param_2[0x1a] = (char)(param_1[5] >> 0x28);
  param_2[0x1b] = (char)*(undefined4 *)((long)param_1 + 0x2c);
  param_2[0x1c] = (char)(param_1[5] >> 0x18);
  param_2[0x1d] = (char)(param_1[5] >> 0x10);
  param_2[0x1e] = (char)(param_1[5] >> 8);
  param_2[0x1f] = (char)param_1[5];
  param_2[0x20] = *(undefined1 *)((long)param_1 + 0x37);
  param_2[0x21] = (char)*(undefined2 *)((long)param_1 + 0x36);
  param_2[0x22] = (char)(param_1[6] >> 0x28);
  param_2[0x23] = (char)*(undefined4 *)((long)param_1 + 0x34);
  param_2[0x24] = (char)(param_1[6] >> 0x18);
  param_2[0x25] = (char)(param_1[6] >> 0x10);
  param_2[0x26] = (char)(param_1[6] >> 8);
  param_2[0x27] = (char)param_1[6];
  param_2[0x28] = *(undefined1 *)((long)param_1 + 0x3f);
  param_2[0x29] = (char)*(undefined2 *)((long)param_1 + 0x3e);
  param_2[0x2a] = (char)(param_1[7] >> 0x28);
  param_2[0x2b] = (char)*(undefined4 *)((long)param_1 + 0x3c);
  param_2[0x2c] = (char)(param_1[7] >> 0x18);
  param_2[0x2d] = (char)(param_1[7] >> 0x10);
  param_2[0x2e] = (char)(param_1[7] >> 8);
  param_2[0x2f] = (char)param_1[7];
  return 0;
}


