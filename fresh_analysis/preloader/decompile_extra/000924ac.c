// File offset entry: 0x924ac

/* WARNING: Heritage AFTER dead removal. Example location: r0x1601000c : 0x02092270 */
/* WARNING: Restarted to delay deadcode elimination for space: ram */

void FUN_020923ac(uint param_1,uint param_2)

{
  uint uVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  
  func_0x02063504(0x800000a2,1);
  func_0x02063380(0x800000a2,1);
  func_0x02063504(0x800000a3,1);
  func_0x02063380(0x800000a3,0);
  func_0x02063618(0x800000a3,1);
  func_0x020637bc(0x800000a3,1);
  uRam0000000002100888 = 0x16010000;
  uVar1 = 26000000;
  if (param_1 != 0) {
    uVar1 = param_1;
  }
  uVar3 = 4;
  if (0x1c200 < param_2) {
    uVar3 = 2;
  }
  uVar2 = param_2 << (ulong)uVar3;
  uVar3 = 0;
  if (uVar2 != 0) {
    uVar3 = uVar1 / uVar2;
  }
  uVar4 = 3;
  if (0x1c200 < param_2) {
    uVar4 = 1;
  }
  if (param_2 << (ulong)uVar4 <= uVar1 - uVar3 * uVar2) {
    uVar3 = uVar3 + 1;
  }
  uRam000000001601000c = 3;
  uRam0000000016010008 = uRam0000000016010008 | 7;
  sRam0000000016010024 = (ushort)(0x1c200 < param_2) << 1;
  uRam0000000016010000 = uVar3 & 0xff;
  uRam0000000016010004 = uVar3 >> 8 & 0xff;
  return;
}


