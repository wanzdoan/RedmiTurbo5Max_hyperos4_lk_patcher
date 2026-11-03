
undefined8 FUN_ffff0000be014624(undefined8 param_1,uint param_2,undefined1 *param_3)

{
  undefined1 uVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  ushort uVar5;
  uint uVar6;
  
  FUN_ffff0000be060ae0(param_3,0,0x13);
  uVar4 = (uint)((ulong)param_1 >> 0x10);
  uVar3 = ((uint)param_1 & 0xffff) >> (ulong)(param_2 % 10) |
          (uint)param_1 << (ulong)(-(param_2 % 10) & 0xf);
  *param_3 = (&DAT_ffff0000be0c7710)[(ulong)uVar3 & 0x1f];
  uVar2 = param_2 / 10 + (param_2 / 100) * -10;
  param_3[1] = (&DAT_ffff0000be0c7710)[(ulong)(uVar3 >> 3) & 0x1f];
  uVar6 = (uint)((ulong)param_1 >> 0x20);
  param_3[2] = (&DAT_ffff0000be0c7710)[(ulong)(uVar3 >> 8) & 0x1f];
  uVar4 = (uVar4 & 0xffff) >> (ulong)(uVar2 & 0xf) | uVar4 << (ulong)(-uVar2 & 0xf);
  param_3[3] = (&DAT_ffff0000be0c7710)[(ulong)(uVar3 >> 0xb) & 0x1f];
  param_3[4] = (&DAT_ffff0000be0c7710)[(ulong)uVar4 & 0x1f];
  uVar2 = param_2 / 100 + (int)(((ulong)param_2 * 0x51eb851f) / 0x14000000000) * -10;
  param_3[5] = (&DAT_ffff0000be0c7710)[(ulong)(uVar4 >> 3) & 0x1f];
  param_3[6] = (&DAT_ffff0000be0c7710)[(ulong)(uVar4 >> 8) & 0x1f];
  uVar3 = (uVar6 & 0xffff) >> (ulong)(uVar2 & 0xf) | uVar6 << (ulong)(-uVar2 & 0xf);
  param_3[7] = (&DAT_ffff0000be0c7710)[(ulong)(uVar4 >> 0xb) & 0x1f];
  param_3[8] = (&DAT_ffff0000be0c7710)[(ulong)uVar3 & 0x1f];
  uVar2 = param_2 / 1000 + (int)(((ulong)param_2 * 0x10624dd3) / 0x28000000000) * -10;
  param_3[9] = (&DAT_ffff0000be0c7710)[(ulong)(uVar3 >> 3) & 0x1f];
  uVar5 = (ushort)((ulong)param_1 >> 0x30);
  param_3[10] = (&DAT_ffff0000be0c7710)[(ulong)(uVar3 >> 8) & 0x1f];
  uVar2 = (uint)(uVar5 >> (ulong)(uVar2 & 0xf)) | (uint)uVar5 << (ulong)(-uVar2 & 0xf);
  param_3[0xb] = (&DAT_ffff0000be0c7710)[(ulong)(uVar3 >> 0xb) & 0x1f];
  param_3[0xc] = (&DAT_ffff0000be0c7710)[(ulong)uVar2 & 0x1f];
  param_3[0xd] = (&DAT_ffff0000be0c7710)[(ulong)(uVar2 >> 3) & 0x1f];
  param_3[0xe] = (&DAT_ffff0000be0c7710)[(ulong)(uVar2 >> 8) & 0x1f];
  uVar1 = (&DAT_ffff0000be0c7710)[(ulong)(uVar2 >> 0xb) & 0x1f];
  param_3[0x10] = 0;
  param_3[0xf] = uVar1;
  return 0;
}


