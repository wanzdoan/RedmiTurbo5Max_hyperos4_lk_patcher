
void FUN_ffff0000be08b770(void)

{
  byte *pbVar1;
  byte bVar2;
  ulong uVar3;
  bool bVar4;
  int iVar5;
  ulong uVar6;
  long lVar7;
  uint uVar8;
  uint uVar9;
  byte *pbVar10;
  ulong uVar11;
  
  DAT_ffff0000be1b7408 = DAT_ffff0000be1b7160;
  DAT_ffff0000be1b7410 = DAT_ffff0000be1b7170;
  pbVar1 = DAT_ffff0000be1b7408;
  uVar3 = DAT_ffff0000be1b7410;
  if (DAT_ffff0000be1b7170 != 0) {
    do {
      iVar5 = FUN_ffff0000be089718(0x12);
      pbVar1 = DAT_ffff0000be1b7408;
      uVar3 = DAT_ffff0000be1b7410;
      if (iVar5 != 0) break;
      if (DAT_ffff0000be1b7410 == 0) {
        FUN_ffff0000be05fd98(s___s__end_of_cert__not_found__ffff0000be0c1571,&DAT_ffff0000be0bb811);
        pbVar1 = DAT_ffff0000be1b7408;
        uVar3 = DAT_ffff0000be1b7410;
        break;
      }
      bVar2 = DAT_ffff0000be1b7408[1];
      uVar9 = (uint)bVar2;
      if (((char)bVar2 < '\0') && ((bVar2 & 0x7f) != 0)) {
        iVar5 = (uVar9 & 0x7f) + 1;
        uVar8 = uVar9 & 0x7f;
        uVar9 = 0;
        pbVar10 = DAT_ffff0000be1b7408 + 2;
        do {
          uVar8 = uVar8 - 1;
          uVar9 = (uint)*pbVar10 | uVar9 << 8;
          pbVar10 = pbVar10 + 1;
        } while (uVar8 != 0);
      }
      else {
        iVar5 = 1;
      }
      if ((*DAT_ffff0000be1b7408 & 0x20) != 0) {
        uVar9 = 0;
      }
      uVar6 = (ulong)(iVar5 + uVar9 + 1);
      uVar3 = DAT_ffff0000be1b7410 - uVar6;
      if (DAT_ffff0000be1b7410 < uVar6) {
        DAT_ffff0000be1b7410 = 0;
        uVar3 = DAT_ffff0000be1b7410;
        break;
      }
      pbVar1 = DAT_ffff0000be1b7408 + uVar6;
      if (*DAT_ffff0000be1b7408 != 3) break;
      bVar2 = DAT_ffff0000be1b7408[1];
      if (((char)bVar2 < '\0') && (uVar11 = (ulong)bVar2 & 0x7f, (int)uVar11 != 0)) {
        uVar9 = 0;
        lVar7 = uVar11 + 1;
        pbVar10 = DAT_ffff0000be1b7408 + 2;
        do {
          uVar8 = (int)uVar11 - 1;
          uVar11 = (ulong)uVar8;
          uVar9 = (uint)*pbVar10 | uVar9 << 8;
          pbVar10 = pbVar10 + 1;
        } while (uVar8 != 0);
      }
      else {
        uVar9 = (uint)bVar2;
        lVar7 = 1;
      }
      if (uVar9 - 1 < 0x41) {
        pbVar10 = DAT_ffff0000be1b7408 + lVar7 + 2;
        DAT_ffff0000be1b7408 = pbVar1;
        DAT_ffff0000be1b7410 = uVar3;
        FUN_ffff0000be087428(&DAT_ffff0000be1b70d0,pbVar10);
        DAT_ffff0000be1b7408 = (byte *)0x0;
        DAT_ffff0000be1b7410 = 0;
        return;
      }
      bVar4 = DAT_ffff0000be1b7410 != uVar6;
      DAT_ffff0000be1b7408 = pbVar1;
      DAT_ffff0000be1b7410 = uVar3;
    } while (bVar4);
  }
  DAT_ffff0000be1b7410 = uVar3;
  DAT_ffff0000be1b7408 = pbVar1;
  if (DAT_ffff0000be1b6ad0 != 0) {
    FUN_ffff0000be060ae0(PTR_DAT_ffff0000be0f5728 + 0xf0,0,0x40);
    FUN_ffff0000be05fd98(s___s__Decode_checksum_lost__ffff0000be0af263,&DAT_ffff0000be0bb811);
  }
  DAT_ffff0000be1b7408 = (byte *)0x0;
  DAT_ffff0000be1b7410 = 0;
  return;
}


