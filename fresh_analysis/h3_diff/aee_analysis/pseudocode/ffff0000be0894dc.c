
uint FUN_ffff0000be0894dc(undefined8 param_1,uint param_2,uint *param_3)

{
  byte bVar1;
  bool bVar2;
  uint uVar3;
  int iVar4;
  byte *pbVar5;
  byte *pbVar6;
  uint uVar7;
  ulong uVar8;
  long lVar9;
  byte *pbVar10;
  uint uVar11;
  
  *param_3 = 0;
  DAT_ffff0000be1b7408 = DAT_ffff0000be1b7160;
  DAT_ffff0000be1b7410 = DAT_ffff0000be1b7170;
  if (DAT_ffff0000be1b7170 == 0) {
    uVar3 = 0;
  }
  else {
    uVar11 = 0x101028;
    do {
      uVar3 = FUN_ffff0000be089718(0xe);
      if (uVar3 != 0) {
        iVar4 = FUN_ffff0000be0879f0();
        if (iVar4 == 0) {
          DAT_ffff0000be1b7408 = (byte *)0x0;
          DAT_ffff0000be1b7410 = 0;
          return uVar3;
        }
        FUN_ffff0000be05fd98(s_Failed_in_asn1_get_oid__ffff0000be0c6d3d);
        DAT_ffff0000be1b7408 = (byte *)0x0;
        DAT_ffff0000be1b7410 = 0;
        return uVar3;
      }
      if (DAT_ffff0000be1b7410 == 0) {
        FUN_ffff0000be05fd98(s___s__end_of_cert__not_found__ffff0000be0c1571,&DAT_ffff0000be0bb811);
        uVar11 = 0x700e;
        iVar4 = FUN_ffff0000be0879f0();
joined_r0xffff0000be0896a0:
        if (iVar4 == 0) {
          DAT_ffff0000be1b7408 = (byte *)0x0;
          DAT_ffff0000be1b7410 = 0;
          return uVar11;
        }
        FUN_ffff0000be05fd98(s_Failed_in_asn1_get_next_obj_ffff0000be0ab066);
        DAT_ffff0000be1b7408 = (byte *)0x0;
        DAT_ffff0000be1b7410 = 0;
        return uVar11;
      }
      pbVar5 = DAT_ffff0000be1b7408 + 1;
      bVar1 = *pbVar5;
      uVar3 = (uint)bVar1;
      if (((char)bVar1 < '\0') && ((bVar1 & 0x7f) != 0)) {
        iVar4 = (uVar3 & 0x7f) + 1;
        uVar7 = uVar3 & 0x7f;
        uVar3 = 0;
        pbVar10 = DAT_ffff0000be1b7408 + 2;
        do {
          uVar7 = uVar7 - 1;
          uVar3 = (uint)*pbVar10 | uVar3 << 8;
          pbVar10 = pbVar10 + 1;
        } while (uVar7 != 0);
      }
      else {
        iVar4 = 1;
      }
      if ((*DAT_ffff0000be1b7408 & 0x20) != 0) {
        uVar3 = 0;
      }
      uVar8 = (ulong)(iVar4 + uVar3 + 1);
      bVar2 = DAT_ffff0000be1b7410 < uVar8;
      DAT_ffff0000be1b7410 = DAT_ffff0000be1b7410 - uVar8;
      if (bVar2) {
        DAT_ffff0000be1b7410 = 0;
        iVar4 = FUN_ffff0000be0879f0();
        goto joined_r0xffff0000be0896a0;
      }
      pbVar10 = DAT_ffff0000be1b7408 + uVar8;
      if (*DAT_ffff0000be1b7408 != 0x13) {
        uVar11 = 0x700f;
        DAT_ffff0000be1b7408 = pbVar10;
        iVar4 = FUN_ffff0000be0879f0();
        goto joined_r0xffff0000be0896a0;
      }
      if (param_2 == 0) {
        uVar3 = 9;
        DAT_ffff0000be1b7408 = pbVar10;
      }
      else {
        bVar1 = *pbVar5;
        if (((char)bVar1 < '\0') && (uVar8 = (ulong)bVar1 & 0x7f, (int)uVar8 != 0)) {
          uVar7 = 0;
          lVar9 = uVar8 + 1;
          pbVar6 = DAT_ffff0000be1b7408 + 2;
          do {
            uVar3 = (int)uVar8 - 1;
            uVar8 = (ulong)uVar3;
            uVar7 = (uint)*pbVar6 | uVar7 << 8;
            pbVar6 = pbVar6 + 1;
          } while (uVar3 != 0);
        }
        else {
          uVar7 = (uint)bVar1;
          lVar9 = 1;
        }
        uVar3 = 0x1028;
        DAT_ffff0000be1b7408 = pbVar10;
        *param_3 = uVar7;
        if (uVar7 <= param_2) {
          FUN_ffff0000be087428(param_1,pbVar5 + lVar9,uVar7);
          DAT_ffff0000be1b7408 = (byte *)0x0;
          DAT_ffff0000be1b7410 = 0;
          return 0;
        }
      }
      uVar3 = uVar3 | 0x100000;
      iVar4 = FUN_ffff0000be0879f0();
      if (iVar4 != 0) {
        FUN_ffff0000be05fd98(s_Parse_socid_in_printable_string_f_ffff0000be0ae126);
      }
    } while (DAT_ffff0000be1b7410 != 0);
  }
  iVar4 = FUN_ffff0000be0879f0();
  if (iVar4 != 0) {
    FUN_ffff0000be05fd98(s_Image_is_not_signed_with_socid__ffff0000be0a3f69);
  }
  DAT_ffff0000be1b7408 = (byte *)0x0;
  DAT_ffff0000be1b7410 = 0;
  return uVar3;
}


