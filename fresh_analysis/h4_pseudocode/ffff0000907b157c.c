
void FUN_ffff0000907b157c(void)

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
  
  DAT_ffff00009090cf48 = DAT_ffff00009090cc68;
  DAT_ffff00009090cf50 = DAT_ffff00009090cc78;
  pbVar1 = DAT_ffff00009090cf48;
  uVar3 = DAT_ffff00009090cf50;
  if (DAT_ffff00009090cc78 != 0) {
    do {
      iVar5 = FUN_ffff0000907af524(0x12);
      pbVar1 = DAT_ffff00009090cf48;
      uVar3 = DAT_ffff00009090cf50;
      if (iVar5 != 0) break;
      if (DAT_ffff00009090cf50 == 0) {
        FUN_ffff00009077e11c(s___s__end_of_cert__not_found__ffff0000907fad1c,&DAT_ffff0000907f228a);
        pbVar1 = DAT_ffff00009090cf48;
        uVar3 = DAT_ffff00009090cf50;
        break;
      }
      bVar2 = DAT_ffff00009090cf48[1];
      uVar9 = (uint)bVar2;
      if (((char)bVar2 < '\0') && ((bVar2 & 0x7f) != 0)) {
        iVar5 = (uVar9 & 0x7f) + 1;
        uVar8 = uVar9 & 0x7f;
        uVar9 = 0;
        pbVar10 = DAT_ffff00009090cf48 + 2;
        do {
          uVar8 = uVar8 - 1;
          uVar9 = (uint)*pbVar10 | uVar9 << 8;
          pbVar10 = pbVar10 + 1;
        } while (uVar8 != 0);
      }
      else {
        iVar5 = 1;
      }
      if ((*DAT_ffff00009090cf48 & 0x20) != 0) {
        uVar9 = 0;
      }
      uVar6 = (ulong)(iVar5 + uVar9 + 1);
      uVar3 = DAT_ffff00009090cf50 - uVar6;
      if (DAT_ffff00009090cf50 < uVar6) {
        DAT_ffff00009090cf50 = 0;
        uVar3 = DAT_ffff00009090cf50;
        break;
      }
      pbVar1 = DAT_ffff00009090cf48 + uVar6;
      if (*DAT_ffff00009090cf48 != 3) break;
      bVar2 = DAT_ffff00009090cf48[1];
      if (((char)bVar2 < '\0') && (uVar11 = (ulong)bVar2 & 0x7f, (int)uVar11 != 0)) {
        uVar9 = 0;
        lVar7 = uVar11 + 1;
        pbVar10 = DAT_ffff00009090cf48 + 2;
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
        pbVar10 = DAT_ffff00009090cf48 + lVar7 + 2;
        DAT_ffff00009090cf48 = pbVar1;
        DAT_ffff00009090cf50 = uVar3;
        FUN_ffff0000907ad234(&DAT_ffff00009090cbd8,pbVar10);
        DAT_ffff00009090cf48 = (byte *)0x0;
        DAT_ffff00009090cf50 = 0;
        return;
      }
      bVar4 = DAT_ffff00009090cf50 != uVar6;
      DAT_ffff00009090cf48 = pbVar1;
      DAT_ffff00009090cf50 = uVar3;
    } while (bVar4);
  }
  DAT_ffff00009090cf50 = uVar3;
  DAT_ffff00009090cf48 = pbVar1;
  if (DAT_ffff00009090c5d8 != 0) {
    FUN_ffff00009077ee50(PTR_DAT_ffff000090839ab0 + 0xf0,0,0x40);
    FUN_ffff00009077e11c(s___s__Decode_checksum_lost__ffff0000907e0754,&DAT_ffff0000907f228a);
  }
  DAT_ffff00009090cf48 = (byte *)0x0;
  DAT_ffff00009090cf50 = 0;
  return;
}


