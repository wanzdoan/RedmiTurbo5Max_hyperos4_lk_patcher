// ffff00009079f5b8  size=1380  FUN_ffff00009079f5b8

int FUN_ffff00009079f5b8
              (long param_1,undefined8 param_2,byte *param_3,ulong param_4,ulong param_5,
              ulong param_6,undefined4 *param_7)

{
  byte bVar1;
  int iVar2;
  byte *pbVar3;
  long lVar4;
  long lVar5;
  long lVar6;
  ulong uVar7;
  uint uVar8;
  ulong uVar9;
  byte *pbVar10;
  long lVar11;
  long lVar12;
  byte *pbVar13;
  ulong uVar14;
  undefined8 local_148;
  undefined8 uStack_140;
  undefined8 local_138;
  undefined8 uStack_130;
  undefined8 local_128;
  undefined8 uStack_120;
  undefined8 local_118;
  undefined8 uStack_110;
  undefined8 local_108;
  undefined8 uStack_100;
  undefined8 local_f8;
  undefined8 uStack_f0;
  undefined8 local_e8;
  undefined8 uStack_e0;
  undefined8 local_d8;
  undefined8 uStack_d0;
  undefined8 local_c8;
  undefined8 uStack_c0;
  undefined8 local_b8;
  undefined8 uStack_b0;
  undefined8 local_a8;
  undefined8 uStack_a0;
  undefined8 local_98;
  undefined8 uStack_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  
  local_78 = 0;
  uStack_70 = 0;
  local_88 = 0;
  uStack_80 = 0;
  local_98 = 0;
  uStack_90 = 0;
  local_a8 = 0;
  uStack_a0 = 0;
  local_b8 = 0;
  uStack_b0 = 0;
  local_c8 = 0;
  uStack_c0 = 0;
  local_d8 = 0;
  uStack_d0 = 0;
  local_e8 = 0;
  uStack_e0 = 0;
  local_f8 = 0;
  uStack_f0 = 0;
  local_108 = 0;
  uStack_100 = 0;
  local_118 = 0;
  uStack_110 = 0;
  local_128 = 0;
  uStack_120 = 0;
  local_138 = 0;
  uStack_130 = 0;
  local_148 = 0;
  uStack_140 = 0;
  pbVar3 = (byte *)FUN_ffff00009077d24c(param_4);
  if (pbVar3 == (byte *)0x0) {
    FUN_ffff00009077de54(s_Error__fail_to_allocate_memory_f_ffff0000907e6ee0);
    return -1;
  }
  lVar4 = FUN_ffff00009077d24c(param_4);
  if (lVar4 == 0) {
    FUN_ffff00009077de54(s_Error__fail_to_allocate_memory_f_ffff0000907eba9d);
    thunk_FUN_ffff0000907c2ad8(pbVar3);
    return -2;
  }
  lVar5 = FUN_ffff00009077d24c(param_4);
  if (lVar5 == 0) {
    FUN_ffff00009077de54(s_Error__fail_to_allocate_memory_f_ffff0000907da080);
    thunk_FUN_ffff0000907c2ad8(pbVar3);
    thunk_FUN_ffff0000907c2ad8(lVar4);
    return -3;
  }
  lVar6 = FUN_ffff00009077d24c(param_4);
  if (lVar6 == 0) {
    FUN_ffff00009077de54(s_Error__fail_to_allocate_memory_f_ffff0000907da0d0);
    thunk_FUN_ffff0000907c2ad8(pbVar3);
    thunk_FUN_ffff0000907c2ad8(lVar4);
    thunk_FUN_ffff0000907c2ad8(lVar5);
    return -4;
  }
  FUN_ffff00009077eb88(lVar6,0,param_4);
  if ((param_1 == 0) || (param_7 == (undefined4 *)0x0)) {
    FUN_ffff00009077de54(s_Error__wrong_input_parameter_for_ffff0000907dbb0a);
    thunk_FUN_ffff0000907c2ad8(pbVar3);
    thunk_FUN_ffff0000907c2ad8(lVar4);
    thunk_FUN_ffff0000907c2ad8(lVar5);
    thunk_FUN_ffff0000907c2ad8(lVar6);
    return -5;
  }
  uVar7 = param_6 >> 3;
  if ((param_6 & 7) != 0) {
    uVar7 = uVar7 + 1;
  }
  *param_7 = 0;
  if (param_4 < uVar7) {
    FUN_ffff00009077de54(s_Error__input_modules_length___lu_ffff000090801855,uVar7,0x200);
    thunk_FUN_ffff0000907c2ad8(pbVar3);
    thunk_FUN_ffff0000907c2ad8(lVar4);
    thunk_FUN_ffff0000907c2ad8(lVar5);
    thunk_FUN_ffff0000907c2ad8(lVar6);
    return -6;
  }
  if (((uVar7 != param_4) || (uVar7 < param_5)) || (uVar7 < param_5 + 0x32)) {
    FUN_ffff00009077de54
              (s_Error__wrong_input_parameter_len_ffff0000907e87c8,uVar7,0x30,param_5 & 0xffffffff,
               param_4 & 0xffffffff);
    thunk_FUN_ffff0000907c2ad8(pbVar3);
    thunk_FUN_ffff0000907c2ad8(lVar4);
    thunk_FUN_ffff0000907c2ad8(lVar5);
    thunk_FUN_ffff0000907c2ad8(lVar6);
    return -8;
  }
  if (param_3[param_4 - 1] != 0xbc) {
    FUN_ffff00009077de54(s_Error__fail_to_check_0xBC_for_pk_ffff0000907f73cc);
    iVar2 = -9;
    goto LAB_ffff00009079f880;
  }
  uVar7 = param_4 - 0x31;
  FUN_ffff00009077e858(pbVar3,param_3);
  FUN_ffff00009077e858(lVar5,param_3 + uVar7,0x30);
  uVar8 = ((int)param_4 * 8 - (int)param_6) + 1;
  if ((-0x100 >> (uVar8 & 0x1f) & (uint)*param_3) != 0) {
    FUN_ffff00009077de54(s_Error__fail_to_check_MSB_for_pkc_ffff0000907f394c);
    iVar2 = -10;
    goto LAB_ffff00009079f880;
  }
  iVar2 = FUN_ffff00009079f3fc(lVar5,0x30,lVar6,uVar7);
  if (iVar2 != 0) {
    FUN_ffff00009077de54(s_Error__fail_to_perform_mgf1_for_p_ffff0000907da120);
    goto LAB_ffff00009079f880;
  }
  if (param_4 != 0x31) {
    if (param_4 == 0x32) {
      uVar9 = 0;
    }
    else {
      uVar9 = uVar7 & 0xfffffffffffffffe;
      pbVar10 = (byte *)(lVar6 + 1);
      pbVar13 = pbVar3 + 1;
      uVar14 = uVar9;
      do {
        uVar14 = uVar14 - 2;
        bVar1 = *pbVar10;
        pbVar13[-1] = pbVar13[-1] ^ pbVar10[-1];
        *pbVar13 = *pbVar13 ^ bVar1;
        pbVar10 = pbVar10 + 2;
        pbVar13 = pbVar13 + 2;
      } while (uVar14 != 0);
      if (uVar7 == uVar9) goto LAB_ffff00009079f9ac;
    }
    lVar11 = (param_4 - uVar9) + -0x31;
    pbVar10 = (byte *)(lVar6 + uVar9);
    pbVar13 = pbVar3 + uVar9;
    do {
      lVar11 = lVar11 + -1;
      *pbVar13 = *pbVar13 ^ *pbVar10;
      pbVar10 = pbVar10 + 1;
      pbVar13 = pbVar13 + 1;
    } while (lVar11 != 0);
  }
LAB_ffff00009079f9ac:
  uVar8 = (uint)*pbVar3 & 0xffU >> (ulong)(uVar8 & 0x1f);
  lVar11 = (param_4 - param_5) + -0x32;
  *pbVar3 = (byte)uVar8;
  pbVar10 = pbVar3;
  lVar12 = lVar11;
  if (lVar11 == 0) {
    lVar11 = 1;
  }
  else {
    do {
      if (*pbVar10 != 0) {
        FUN_ffff00009077de54(s_Error__fail_to_check_0x00_and_0x_ffff0000908018c1);
        iVar2 = -0xb;
        goto LAB_ffff00009079f880;
      }
      lVar12 = lVar12 + -1;
      pbVar10 = pbVar10 + 1;
    } while (lVar12 != 0);
    uVar8 = (uint)pbVar3[lVar11];
    lVar11 = (param_4 - param_5) + -0x31;
  }
  if (uVar8 == 1) {
    iVar2 = FUN_ffff0000907a4d78(&local_148);
    if (iVar2 == 0) {
      FUN_ffff00009077eb88(lVar6,0,8);
      iVar2 = FUN_ffff0000907a4dd8(&local_148,lVar6,8);
      if (iVar2 == 0) {
        iVar2 = FUN_ffff0000907a4dd8(&local_148,param_1,param_2);
        if (iVar2 == 0) {
          iVar2 = FUN_ffff0000907a4dd8(&local_148,pbVar3 + lVar11,param_5);
          if (iVar2 == 0) {
            iVar2 = FUN_ffff0000907a4e68(&local_148,lVar6);
            if (iVar2 == 0) {
              iVar2 = FUN_ffff00009077e820(lVar6,lVar5,0x30);
              if (iVar2 == 0) {
                iVar2 = 0;
                *param_7 = 1;
              }
              else {
                iVar2 = 0;
              }
            }
            else {
              FUN_ffff00009077de54(s_Error__fail_to_perform_sha384_do_ffff0000907f086d);
            }
          }
          else {
            FUN_ffff00009077de54(s_Error__fail_to_perform_sha384_pr_ffff0000907dbb4f);
          }
        }
        else {
          FUN_ffff00009077de54(s_Error__fail_to_perform_sha384_pr_ffff0000907f576d);
        }
      }
      else {
        FUN_ffff00009077de54(s_Error__fail_to_perform_sha384_pr_ffff0000908031cb);
      }
    }
    else {
      FUN_ffff00009077de54(s_Error__fail_to_perform_sha384_in_ffff0000907f5722);
    }
  }
  else {
    FUN_ffff00009077de54(s_Error__fail_to_0x01_in_BD_for_pk_ffff0000907faacc);
    iVar2 = -0xc;
  }
LAB_ffff00009079f880:
  FUN_ffff00009077eb88(pbVar3,0,param_4);
  FUN_ffff00009077eb88(lVar6,0,param_4);
  FUN_ffff00009077eb88(lVar4,0,param_4);
  FUN_ffff00009077eb88(lVar5,0,param_4);
  thunk_FUN_ffff0000907c2ad8(pbVar3);
  thunk_FUN_ffff0000907c2ad8(lVar4);
  thunk_FUN_ffff0000907c2ad8(lVar5);
  thunk_FUN_ffff0000907c2ad8(lVar6);
  return iVar2;
}


