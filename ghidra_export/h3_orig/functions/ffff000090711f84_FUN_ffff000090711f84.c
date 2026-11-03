// ffff000090711f84  size=808  FUN_ffff000090711f84

undefined8 FUN_ffff000090711f84(byte *param_1,long param_2,int param_3,undefined4 param_4)

{
  ulong uVar1;
  uint uVar2;
  byte bVar3;
  byte bVar4;
  int iVar5;
  int iVar6;
  uint uVar7;
  char *pcVar8;
  uint *puVar9;
  long lVar10;
  byte *local_c0;
  undefined8 local_b8;
  undefined8 local_b0;
  long local_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  undefined8 local_90;
  undefined8 local_88;
  code *local_80;
  code *local_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  
  iVar5 = FUN_ffff0000907167d4();
  bVar3 = param_1[3];
  iVar6 = (uint)(CONCAT11(*param_1,param_1[1]) == 0x1f8b) << 1;
  if (((uint)param_1[2] << 0x10 | (uint)param_1[1] << 8 | (uint)bVar3 << 0x18 | (uint)*param_1) ==
      0x184c2102) {
    iVar6 = 1;
  }
  if (iVar6 == 1) {
    if (param_2 == 0) {
      pcVar8 = s__s_error__NULL_output_pointer_ffff0000907cf198;
    }
    else if (*(int *)param_1 == 0x184c2102) {
      puVar9 = (uint *)(param_1 + 4);
      lVar10 = (long)param_3 - 4;
      uVar2 = *puVar9;
      while( true ) {
        while (uVar2 == 0x184c2102) {
          puVar9 = puVar9 + 1;
          lVar10 = lVar10 + -4;
          uVar2 = *puVar9;
        }
        uVar7 = FUN_ffff00009076f410(puVar9 + 1,param_2,uVar2,0x800000);
        pcVar8 = s__s_error__decodes_failure_ffff0000907f25a1;
        if ((int)uVar7 < 0) break;
        lVar10 = (lVar10 - (ulong)uVar2) + -4;
        if (lVar10 == 0) goto LAB_ffff000090712018;
        pcVar8 = s__s_error__data_corrupt_ffff0000907f3fc8;
        if (lVar10 < 0) break;
        puVar9 = (uint *)((long)(puVar9 + 1) + (ulong)uVar2);
        param_2 = param_2 + (ulong)uVar7;
        uVar2 = *puVar9;
      }
    }
    else {
      pcVar8 = s__s_error__invalid_header_ffff0000907e41ee;
    }
    FUN_ffff00009077de54(pcVar8,s_unlz4_ffff000090800334);
    pcVar8 = s_lz4_decompress_failure_ffff0000907f0c46;
  }
  else {
    if (iVar6 != 2) {
      if (iVar6 == 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff00009077cc18(s_No_magic_number_of_compression_i_ffff0000907dbf4c);
      }
LAB_ffff000090712018:
      iVar6 = FUN_ffff0000907167d4();
      FUN_ffff00009077de54
                (s__PROFILE_______s_takes__d_ms_ffff0000907d8a95,
                 s_decompress_kernel_ffff000090800322,iVar6 - iVar5);
      return 0;
    }
    if ((param_1[2] == 8) && (bVar3 < 0x20)) {
      if ((bVar3 >> 2 & 1) == 0) {
        bVar4 = bVar3 >> 3 & 1;
        uVar1 = 10;
      }
      else {
        bVar4 = bVar3 >> 3 & 1;
        uVar1 = (ulong)*(ushort *)(param_1 + 10) + 0xc;
      }
      while (bVar4 != 0) {
        bVar4 = param_1[uVar1];
        uVar1 = uVar1 + 1;
      }
      bVar4 = bVar3 >> 4 & 1;
      while (bVar4 != 0) {
        bVar4 = param_1[uVar1];
        uVar1 = uVar1 + 1;
      }
      if ((bVar3 & 2) != 0) {
        uVar1 = uVar1 + 2;
      }
      if (uVar1 < (ulong)(long)param_3) {
        local_60 = 0;
        uStack_58 = 0;
        local_70 = 0;
        uStack_68 = 0;
        local_80 = (code *)0x0;
        local_78 = (code *)0x0;
        local_90 = 0;
        local_88 = 0;
        local_a0 = 0;
        uStack_98 = 0;
        local_b0 = 0;
        local_a8 = 0;
        local_c0 = (byte *)0x0;
        local_b8 = 0;
        FUN_ffff00009077eb88(&local_c0,0,0x70);
        iVar6 = FUN_ffff0000907761d4(&local_c0,0xfffffff1,s_1_3_1_1_motley_ffff0000907ebdd0,0x70);
        if (iVar6 == 0) {
          local_c0 = param_1 + (int)uVar1;
          local_80 = FUN_ffff0000907122ac;
          local_78 = FUN_ffff0000907122c4;
          local_a0 = CONCAT44(local_a0._4_4_,param_4);
          local_b8 = CONCAT44(local_b8._4_4_,param_3 - (int)uVar1);
          local_a8 = param_2;
          iVar6 = FUN_ffff0000907763b0(&local_c0,4);
          if (iVar6 == 1) {
            FUN_ffff0000907782f4(&local_c0);
            goto LAB_ffff000090712018;
          }
          FUN_ffff00009077de54(s_inflate___error__return__d_ffff0000907fca50,iVar6);
          FUN_ffff0000907782f4(&local_c0);
        }
        else {
          (*local_78)(local_70,local_88);
          FUN_ffff00009077de54(s_inflateInit2___error__returned___ffff0000907e250d,iVar6);
        }
      }
      else {
        FUN_ffff00009077de54
                  (s__s_header_is_bigger_than_expecta_ffff00009080033a,s_gunzip_ffff0000907df3ff);
      }
    }
    else {
      FUN_ffff00009077de54(s_gzip_header_is_not_correct__Erro_ffff000090801dae);
    }
    pcVar8 = s_gzip_decompress_failure_ffff0000907f2588;
  }
  FUN_ffff00009077de54(pcVar8);
  return 0xffffffff;
}


