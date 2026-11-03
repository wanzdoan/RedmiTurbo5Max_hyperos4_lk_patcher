
void FUN_ffff0000b806dd74(void)

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
  
  DAT_ffff0000b81b9148 = DAT_ffff0000b81b8ea0;
  DAT_ffff0000b81b9150 = DAT_ffff0000b81b8eb0;
  pbVar1 = DAT_ffff0000b81b9148;
  uVar3 = DAT_ffff0000b81b9150;
  if (DAT_ffff0000b81b8eb0 != 0) {
    do {
      iVar5 = FUN_ffff0000b806bd1c(0x12);
      pbVar1 = DAT_ffff0000b81b9148;
      uVar3 = DAT_ffff0000b81b9150;
      if (iVar5 != 0) break;
      if (DAT_ffff0000b81b9150 == 0) {
        FUN_ffff0000b804c33c(s___s__end_of_cert__not_found__ffff0000b809d1a1,&DAT_ffff0000b80987e6);
        pbVar1 = DAT_ffff0000b81b9148;
        uVar3 = DAT_ffff0000b81b9150;
        break;
      }
      bVar2 = DAT_ffff0000b81b9148[1];
      uVar9 = (uint)bVar2;
      if (((char)bVar2 < '\0') && ((bVar2 & 0x7f) != 0)) {
        iVar5 = (uVar9 & 0x7f) + 1;
        uVar8 = uVar9 & 0x7f;
        uVar9 = 0;
        pbVar10 = DAT_ffff0000b81b9148 + 2;
        do {
          uVar8 = uVar8 - 1;
          uVar9 = (uint)*pbVar10 | uVar9 << 8;
          pbVar10 = pbVar10 + 1;
        } while (uVar8 != 0);
      }
      else {
        iVar5 = 1;
      }
      if ((*DAT_ffff0000b81b9148 & 0x20) != 0) {
        uVar9 = 0;
      }
      uVar6 = (ulong)(iVar5 + uVar9 + 1);
      uVar3 = DAT_ffff0000b81b9150 - uVar6;
      if (DAT_ffff0000b81b9150 < uVar6) {
        DAT_ffff0000b81b9150 = 0;
        uVar3 = DAT_ffff0000b81b9150;
        break;
      }
      pbVar1 = DAT_ffff0000b81b9148 + uVar6;
      if (*DAT_ffff0000b81b9148 != 3) break;
      bVar2 = DAT_ffff0000b81b9148[1];
      if (((char)bVar2 < '\0') && (uVar11 = (ulong)bVar2 & 0x7f, (int)uVar11 != 0)) {
        uVar9 = 0;
        lVar7 = uVar11 + 1;
        pbVar10 = DAT_ffff0000b81b9148 + 2;
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
        pbVar10 = DAT_ffff0000b81b9148 + lVar7 + 2;
        DAT_ffff0000b81b9148 = pbVar1;
        DAT_ffff0000b81b9150 = uVar3;
        FUN_ffff0000b8069a2c(&DAT_ffff0000b81b8e10,pbVar10);
        DAT_ffff0000b81b9148 = (byte *)0x0;
        DAT_ffff0000b81b9150 = 0;
        return;
      }
      bVar4 = DAT_ffff0000b81b9150 != uVar6;
      DAT_ffff0000b81b9148 = pbVar1;
      DAT_ffff0000b81b9150 = uVar3;
    } while (bVar4);
  }
  DAT_ffff0000b81b9150 = uVar3;
  DAT_ffff0000b81b9148 = pbVar1;
  if (DAT_ffff0000b81b8810 != 0) {
    FUN_ffff0000b804d070(PTR_DAT_ffff0000b81087f8 + 0xf0,0,0x40);
    FUN_ffff0000b804c33c(s___s__Decode_checksum_lost__ffff0000b808e746,&DAT_ffff0000b80987e6);
  }
  DAT_ffff0000b81b9148 = (byte *)0x0;
  DAT_ffff0000b81b9150 = 0;
  return;
}


