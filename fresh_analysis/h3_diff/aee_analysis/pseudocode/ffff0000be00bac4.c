
undefined8 FUN_ffff0000be00bac4(ulong *param_1,long *param_2,undefined4 *param_3)

{
  long *plVar1;
  undefined8 *puVar2;
  ulong *puVar3;
  ulong *puVar4;
  ulong *puVar5;
  byte bVar6;
  byte bVar7;
  long lVar8;
  long lVar9;
  uint uVar10;
  short sVar11;
  ushort uVar12;
  long lVar13;
  bool bVar14;
  undefined1 uVar15;
  undefined2 uVar17;
  int iVar18;
  undefined4 uVar19;
  uint uVar20;
  uint uVar21;
  int iVar22;
  int iVar23;
  int iVar24;
  undefined4 *puVar25;
  long lVar26;
  undefined8 uVar27;
  long lVar28;
  char *pcVar29;
  undefined1 uVar30;
  ulong uVar31;
  ulong uVar32;
  ulong *puVar33;
  ulong uVar34;
  ushort *puVar35;
  ulong uVar36;
  char *pcVar37;
  ulong *puVar38;
  ulong local_1b0;
  long local_1a0;
  long local_180;
  long local_168;
  long *local_138;
  undefined8 local_130;
  undefined4 local_128;
  undefined8 local_120;
  undefined8 uStack_118;
  undefined8 local_110;
  undefined8 uStack_108;
  undefined4 *local_100;
  undefined8 local_f8;
  undefined8 local_f0;
  long local_e8;
  undefined8 local_e0;
  undefined8 uStack_d8;
  undefined8 local_d0;
  undefined8 uStack_c8;
  undefined8 local_c0;
  undefined8 uStack_b8;
  undefined8 local_b0;
  undefined8 uStack_a8;
  undefined8 local_a0;
  undefined8 uStack_98;
  long local_90;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined2 uVar16;
  
  local_110 = 0;
  uStack_108 = 0;
  local_120 = 0;
  uStack_118 = 0;
  if (0x7f < (uint)param_1[3]) {
    FUN_ffff0000be0115fc(s_Too_many_zip_entry__d_ffff0000be0a30ec);
    puVar25 = param_3;
    goto LAB_ffff0000be00c0a4;
  }
  local_a0 = 0;
  uStack_98 = 0;
  local_b0 = 0;
  uStack_a8 = 0;
  local_c0 = 0;
  uStack_b8 = 0;
  local_d0 = 0;
  uStack_c8 = 0;
  local_e0 = 0;
  uStack_d8 = 0;
  local_f0 = 0;
  local_e8 = 0;
  local_100 = (undefined4 *)0x0;
  local_f8 = 0;
  local_130 = 0;
  local_128 = 0;
  FUN_ffff0000be0114f0
            (s__s__zf__p__p___s_ffff0000be0b082c,s_kzip_add_file_ffff0000be0c2888,param_1,
             param_1[0x40e],param_3);
  iVar18 = FUN_ffff0000be064ad8(s_mrdump_compression_method_ffff0000be0bccd2,&local_120,0x20);
  if ((iVar18 == 0) &&
     (iVar18 = FUN_ffff0000be060f54(&local_120,s_store_ffff0000be0c1848,5), iVar18 == 0)) {
    uVar21 = 0;
    pcVar37 = s__s__No_compression_ffff0000be0a7a04;
  }
  else {
    uVar21 = 1;
    pcVar37 = s__s__Compression_ffff0000be0b083e;
  }
  FUN_ffff0000be0114f0(pcVar37,s_kzip_add_file_ffff0000be0c2888);
  uVar10 = (uint)param_1[3];
  *(uint *)(param_1 + 3) = uVar10 + 1;
  FUN_ffff0000be029a20(&local_130);
  *(ushort *)(param_1 + (ulong)uVar10 * 7 + 10) =
       ((ushort)((local_128 & 0xf) << 5) | local_128._2_2_ << 9 | local_130._6_2_ & 0x1f) + 0x8800;
  uVar31 = param_1[2];
  param_1[(ulong)uVar10 * 7 + 4] = (ulong)param_3;
  puVar38 = param_1 + (ulong)uVar10 * 7 + 5;
  *(uint *)puVar38 = uVar21;
  param_1[(ulong)uVar10 * 7 + 6] = uVar31;
  puVar35 = (ushort *)((long)param_1 + (ulong)uVar10 * 0x38 + 0x4c);
  *puVar35 = (local_130._2_2_ & 0x3f) << 5 | local_130._4_2_ << 0xb | (ushort)local_130 >> 1 & 0x1f;
  puVar33 = param_1 + (ulong)uVar10 * 7 + 9;
  *(uint *)puVar33 = 0xffffffff;
  FUN_ffff0000be05fd98
            (s__s__d__kedump__current_time____d_ffff0000be0a4299,s_kzip_add_file_ffff0000be0c2888,
             0x484);
  puVar25 = (undefined4 *)FUN_ffff0000be05f1bc(0x528);
  if (puVar25 == (undefined4 *)0x0) {
    FUN_ffff0000be05fd98
              (s__s__d__zip_localheader_alloc_fai_ffff0000be0a7a18,s_kzip_add_file_ffff0000be0c2888,
               0x487);
    return 0;
  }
  uVar31 = param_1[0x388];
  uVar21 = (uint)*puVar38;
  *puVar25 = 0x4034b50;
  if ((int)uVar31 == 0) {
    iVar18 = 0;
    uVar30 = 0;
    uVar15 = 0x2d;
  }
  else {
    uVar16 = FUN_ffff0000be05f5e0(&DAT_ffff0000be0ac457,0,0);
    uVar15 = (undefined1)uVar16;
    iVar18 = (int)param_1[0x388];
    uVar30 = (undefined1)((ushort)uVar16 >> 8);
  }
  *(undefined1 *)((long)puVar25 + 5) = uVar30;
  bVar14 = (uVar21 & 0xfffffffe) == 8;
  sVar11 = (short)param_1[(ulong)uVar10 * 7 + 10];
  uVar12 = *puVar35;
  *(undefined1 *)(puVar25 + 1) = uVar15;
  bVar6 = bVar14 << 1 | 4;
  if (uVar21 != 2) {
    bVar6 = bVar14 << 1;
  }
  bVar7 = 0xe;
  if (uVar21 != 1) {
    bVar7 = bVar6 | 8;
  }
  *(undefined2 *)((long)puVar25 + 7) = 0x800;
  *(ushort *)((long)puVar25 + 10) = uVar12;
  *(byte *)((long)puVar25 + 6) = bVar7 | iVar18 != 0;
  *(char *)((long)puVar25 + 0xd) = (char)((ushort)sVar11 >> 8);
  *(undefined1 *)((long)puVar25 + 9) = 0;
  uVar16 = 0x43c;
  if (iVar18 == 0) {
    uVar16 = 0x14;
  }
  *(char *)(puVar25 + 3) = (char)sVar11;
  *(undefined8 *)((long)puVar25 + 0xe) = 0xffffffff00000000;
  *(undefined4 *)((long)puVar25 + 0x16) = 0xffffffff;
  uVar17 = FUN_ffff0000be060e5c(param_3);
  *(char *)(puVar25 + 7) = (char)uVar16;
  lVar28 = (long)puVar25 + 0x1e;
  *(char *)((long)puVar25 + 0x1a) = (char)uVar17;
  *(char *)((long)puVar25 + 0x1b) = (char)((ushort)uVar17 >> 8);
  *(char *)((long)puVar25 + 0x1d) = (char)((ushort)uVar16 >> 8);
  if (iVar18 == 0) {
    uVar27 = FUN_ffff0000be060e5c(param_3);
    FUN_ffff0000be0607b0(lVar28,param_3,uVar27);
LAB_ffff0000be00be58:
    lVar26 = FUN_ffff0000be060e5c(param_3);
    puVar2 = (undefined8 *)(lVar28 + lVar26);
    uVar31 = param_1[0x388];
    *(undefined4 *)(puVar2 + 2) = 0;
    *puVar2 = 0x100001;
    puVar2[1] = 0;
    iVar22 = (int)puVar25;
    iVar18 = (int)puVar2;
    if ((int)uVar31 == 0) {
      uVar21 = (iVar18 + 0x14) - iVar22;
    }
    else {
      *(undefined2 *)(puVar2 + 3) = 0;
      *(undefined4 *)((long)puVar2 + 0x14) = 0x22451;
      uVar19 = FUN_ffff0000be0553a4(0,0,0);
      uVar27 = FUN_ffff0000be060e5c(s______BEGIN_PUBLIC_KEY______MIIBI_ffff0000be0b3b9d);
      uVar19 = FUN_ffff0000be0553a4
                         (uVar19,s______BEGIN_PUBLIC_KEY______MIIBI_ffff0000be0b3b9d,uVar27);
      uVar31 = param_1[0x388];
      *(char *)((long)puVar2 + 0x1e) = (char)uVar19;
      *(undefined4 *)((long)puVar2 + 0x1a) = 0x42452;
      *(char *)((long)puVar2 + 0x1f) = (char)((uint)uVar19 >> 8);
      *(char *)(puVar2 + 4) = (char)((uint)uVar19 >> 0x10);
      *(char *)((long)puVar2 + 0x21) = (char)((uint)uVar19 >> 0x18);
      if ((int)uVar31 == 0) {
        uVar21 = (iVar18 + 0x22) - iVar22;
      }
      else {
        *(undefined4 *)((long)puVar2 + 0x22) = 0x62453;
        FUN_ffff0000be0607b0((long)puVar2 + 0x26,&DAT_ffff0000be0af40e,6);
        if ((int)param_1[0x388] == 0) {
          uVar21 = (iVar18 + 0x2c) - iVar22;
        }
        else {
          *(undefined4 *)((long)puVar2 + 0x2c) = 0x1002554;
          lVar28 = FUN_ffff0000be05f1bc(0x100);
          if (lVar28 == 0) {
            FUN_ffff0000be05fd98
                      (s__s__d__alloc_emsg_fail__no_enoug_ffff0000be0b602a,
                       s_put_localheader_ffff0000be0b084f,0x148);
                    /* WARNING: Subroutine does not return */
            FUN_ffff0000be05ee98
                      (s_app_aee_mrdump_zip_c_ffff0000be0bdf6a,0x149,&DAT_ffff0000be0a553d);
          }
          FUN_ffff0000be060ae0(lVar28,0,0x100);
          FUN_ffff0000be0607b0(puVar2 + 6,lVar28,0x100);
          *(undefined4 *)(puVar2 + 0x26) = 0x1002555;
          FUN_ffff0000be060ae0(lVar28,0,0x100);
          FUN_ffff0000be0607b0((long)puVar2 + 0x134,lVar28,0x100);
          thunk_FUN_ffff0000be09937c(lVar28);
          if ((int)param_1[0x388] == 0) {
            uVar21 = (iVar18 + 0x234) - iVar22;
          }
          else {
            *(undefined4 *)((long)puVar2 + 0x234) = 0x1002454;
            lVar28 = FUN_ffff0000be05f1bc(0x100);
            if (lVar28 == 0) {
              FUN_ffff0000be05fd98
                        (s__s__d__alloc_emsg_fail__no_enoug_ffff0000be0b602a,
                         s_put_localheader_ffff0000be0b084f,0x172);
                    /* WARNING: Subroutine does not return */
              FUN_ffff0000be05ee98
                        (s_app_aee_mrdump_zip_c_ffff0000be0bdf6a,0x173,&DAT_ffff0000be0a553d);
            }
            FUN_ffff0000be060ae0(lVar28,0,0x100);
            FUN_ffff0000be0607b0(lVar28,param_1 + 0x3cc,0x100);
            FUN_ffff0000be0607b0(puVar2 + 0x47,lVar28,0x100);
            *(undefined4 *)(puVar2 + 0x67) = 0x1002455;
            FUN_ffff0000be060ae0(lVar28,0,0x100);
            FUN_ffff0000be0607b0(lVar28,param_1 + 0x3ec,0x100);
            FUN_ffff0000be0607b0((long)puVar2 + 0x33c,lVar28,0x100);
            thunk_FUN_ffff0000be09937c(lVar28);
            uVar21 = (iVar18 + 0x43c) - iVar22;
          }
        }
      }
    }
    if (-1 < (int)uVar21) {
      uVar20 = (*(code *)param_1[0x40e])(param_1[0x40d],puVar25,uVar21);
      if ((int)uVar20 < 1) {
        uVar31 = param_1[2];
      }
      else {
        uVar31 = param_1[2] + (ulong)uVar20;
        param_1[2] = uVar31;
      }
      if (uVar31 - *param_1 >> 0x18 != 0) {
        FUN_ffff0000be011708(uVar31 >> 0x14);
        uVar31 = param_1[2];
        *param_1 = uVar31;
      }
      if (0xffff < uVar31 - param_1[1]) {
        FUN_ffff0000be0357ec();
        param_1[1] = param_1[2];
      }
      thunk_FUN_ffff0000be09937c(puVar25);
      if (uVar20 != uVar21) {
        return 0;
      }
      FUN_ffff0000be060ae0(&local_100,0,0x70);
      iVar18 = FUN_ffff0000be057624
                         (&local_100,(uint)*puVar38,8,0xfffffff1,8,0,
                          s_1_3_1_1_motley_ffff0000be0b734e,0x70);
      if (iVar18 != 0) {
        FUN_ffff0000be0115fc(s_zlib_compress_init_failed_ffff0000be0b4f44);
        return 0;
      }
      uVar31 = param_1[2];
      lVar28 = FUN_ffff0000be05f1bc(0x1000);
      puVar25 = (undefined4 *)FUN_ffff0000be05f1bc(0x1000);
      if ((lVar28 != 0) && (puVar25 != (undefined4 *)0x0)) {
        puVar38 = param_1 + 0x389;
        bVar14 = false;
        uVar36 = 0;
        puVar3 = param_1 + 0x38b;
        puVar4 = param_1 + 0x3c8;
        puVar5 = param_1 + 0x40c;
        local_138 = param_2;
        do {
          if (!bVar14) {
            FUN_ffff0000be05fd98
                      (s__s__d__clear_encrypt_buf_at_firs_ffff0000be0a55e6,
                       s_kzip_add_file_ffff0000be0c2888,0x4ad,param_3);
            FUN_ffff0000be0093a0(param_1);
          }
          iVar18 = (int)local_138[2];
          if (iVar18 == 2) {
            lVar26 = *local_138;
            pcVar37 = (char *)local_138[1];
            bVar14 = pcVar37 == (char *)0x0;
            iVar18 = (uint)bVar14 << 2;
            FUN_ffff0000be0114f0(s____Compress_expdb_offset__llx__s_ffff0000be0ac45a,lVar26,pcVar37)
            ;
            do {
              pcVar29 = pcVar37;
              if (0xfff < (long)pcVar37) {
                pcVar29 = (char *)0x1000;
              }
              FUN_ffff0000be00a85c(puVar25,(ulong)pcVar29 & 0xffffffff);
              uVar21 = FUN_ffff0000be0553a4(~(uint)*puVar33,puVar25,(ulong)pcVar29 & 0xffffffff);
              iVar22 = (int)pcVar29;
              pcVar37 = pcVar37 + -(long)iVar22;
              local_f8 = CONCAT44(local_f8._4_4_,iVar22);
              *(uint *)puVar33 = ~uVar21;
              local_100 = puVar25;
              do {
                local_e0 = CONCAT44(local_e0._4_4_,0x1000);
                local_e8 = lVar28;
                FUN_ffff0000be057f18(&local_100,(ulong)bVar14 << 2);
                uVar21 = 0x1000 - (int)local_e0;
                if (0 < (int)uVar21) {
                  if (((int)param_1[0x388] != 0) &&
                     (iVar23 = FUN_ffff0000be0091e4
                                         (lVar28,lVar28,uVar21,puVar3,puVar4,puVar38,puVar5),
                     iVar23 != 0)) {
                    FUN_ffff0000be05fd98
                              (s__s__d__encrypt_fail_ffff0000be0ad296,
                               s_kzip_add_file_from_expdb_ffff0000be0ae3bf,0x43d);
                  }
                  uVar20 = (*(code *)param_1[0x40e])(param_1[0x40d],lVar28,uVar21);
                  if ((int)uVar20 < 1) {
                    uVar34 = param_1[2];
                  }
                  else {
                    uVar34 = param_1[2] + (ulong)uVar20;
                    param_1[2] = uVar34;
                  }
                  if (uVar34 - *param_1 >> 0x18 != 0) {
                    FUN_ffff0000be011708(uVar34 >> 0x14);
                    uVar34 = param_1[2];
                    *param_1 = uVar34;
                  }
                  if (0xffff < uVar34 - param_1[1]) {
                    FUN_ffff0000be0357ec();
                    param_1[1] = param_1[2];
                  }
                  if (uVar20 != uVar21) {
                    FUN_ffff0000be0114f0(s____Compress_expdb_offset__llx__e_ffff0000be0b0878,lVar26)
                    ;
                    pcVar29 = s_error__surplus_size___llu_ffff0000be0c035c;
                    goto LAB_ffff0000be00c970;
                  }
                }
              } while ((int)local_e0 == 0);
              lVar26 = iVar22 + lVar26;
            } while (0 < (long)pcVar37);
            FUN_ffff0000be0114f0(s____Compress_expdb_offset__llx__d_ffff0000be0b2ae1,lVar26,pcVar37)
            ;
          }
          else if (iVar18 == 1) {
            local_180 = *local_138;
            uVar34 = local_138[1];
            local_90 = 0;
            lVar26 = 0x200000;
            if (uVar34 < 0x200001) {
              lVar26 = 0x1000;
            }
            uVar19 = 0x15;
            if (uVar34 < 0x200001) {
              uVar19 = 0;
            }
            iVar22 = 0x200;
            if (uVar34 < 0x200001) {
              iVar22 = 1;
            }
            FUN_ffff0000be0114f0
                      (s____Compress_memory__llx__size__l_ffff0000be0ae39b,local_180,uVar34);
            lVar8 = local_138[3];
            lVar9 = local_138[4];
            lVar13 = local_138[5];
            local_78 = 0;
            uStack_70 = 0;
            local_88 = 0;
            uStack_80 = 0;
            iVar18 = FUN_ffff0000be064ad8(s_mrdump_dump_type_ffff0000be0b854a,&local_88,0x20);
            bVar14 = false;
            if (iVar18 == 0) {
              iVar18 = FUN_ffff0000be060cb8(&local_88,s_linux_kernel_memory_ffff0000be0bccec);
              if ((((iVar18 == 0) && (lVar8 != 0)) && (lVar9 != 0)) && ((int)lVar13 != 0)) {
                pcVar37 = s_dump_linux_kernel_memory___1_ffff0000be0a42cc;
                bVar14 = true;
              }
              else {
                pcVar37 = s_dump_linux_kernel_memory___0_ffff0000be0ab284;
                bVar14 = false;
              }
              FUN_ffff0000be0114f0(pcVar37);
              FUN_ffff0000be0114f0
                        (s_memmap_0x_llx___pageflags_0x_llx_ffff0000be0ab249,lVar8,lVar9,(int)lVar13
                        );
            }
            iVar23 = 0;
            iVar18 = (uint)(uVar34 == 0) << 2;
            local_1a0 = 0;
            local_168 = 0;
            local_1b0 = 0xffffffffffffffff;
            do {
              if (iVar23 == 0) {
                iVar24 = FUN_ffff0000be05d9a8
                                   (PTR_DAT_ffff0000be0f5570,s_coredump_addr_ffff0000be0c3b67,lVar26
                                    ,&local_90,uVar19,local_180,0,0);
                lVar8 = local_90;
                if (iVar24 != 0) {
                  FUN_ffff0000be0115fc(s_vmm_alloc_physical_fail_ffff0000be0b085f);
                  goto LAB_ffff0000be00c990;
                }
                if (bVar14) {
                  uVar27 = FUN_ffff0000be05d228(&DAT_ffff0000be0fa000);
                  local_1b0 = FUN_ffff0000be00b904(local_138,local_180,lVar8,lVar26,uVar27);
                }
                local_1a0 = (local_1b0 & ((long)local_1b0 >> 0x3f ^ 0xffffffffffffffffU)) +
                            local_1a0;
                local_168 = local_90;
              }
              uVar32 = uVar34;
              if (0xfff < uVar34) {
                uVar32 = 0x1000;
              }
              FUN_ffff0000be0607b0(puVar25,local_168,uVar32);
              uVar21 = FUN_ffff0000be0553a4(~(uint)*puVar33,puVar25,uVar32 & 0xffffffff);
              local_168 = local_168 + uVar32;
              local_f8 = CONCAT44(local_f8._4_4_,(int)uVar32);
              *(uint *)puVar33 = ~uVar21;
              uVar34 = uVar34 - uVar32;
              local_100 = puVar25;
              do {
                local_e0 = CONCAT44(local_e0._4_4_,0x1000);
                local_e8 = lVar28;
                FUN_ffff0000be057f18(&local_100,iVar18);
                uVar21 = 0x1000 - (int)local_e0;
                if (0 < (int)uVar21) {
                  if (((int)param_1[0x388] != 0) &&
                     (iVar24 = FUN_ffff0000be0091e4
                                         (lVar28,lVar28,uVar21,puVar3,puVar4,puVar38,puVar5),
                     iVar24 != 0)) {
                    FUN_ffff0000be05fd98
                              (s__s__d__encrypt_fail_ffff0000be0ad296,
                               s_kzip_add_file_do_mapped_ffff0000be0c3b75,0x3fc);
                  }
                  uVar20 = (*(code *)param_1[0x40e])(param_1[0x40d],lVar28,uVar21);
                  if ((int)uVar20 < 1) {
                    uVar32 = param_1[2];
                  }
                  else {
                    uVar32 = param_1[2] + (ulong)uVar20;
                    param_1[2] = uVar32;
                  }
                  if (uVar32 - *param_1 >> 0x18 != 0) {
                    FUN_ffff0000be011708(uVar32 >> 0x14);
                    uVar32 = param_1[2];
                    *param_1 = uVar32;
                  }
                  if (0xffff < uVar32 - param_1[1]) {
                    FUN_ffff0000be0357ec();
                    param_1[1] = param_1[2];
                  }
                  if (uVar20 != uVar21) goto LAB_ffff0000be00c988;
                }
              } while ((int)local_e0 == 0);
              iVar24 = 0;
              if (iVar22 != 0) {
                iVar24 = (iVar23 + 1) / iVar22;
              }
              iVar23 = (iVar23 + 1) - iVar24 * iVar22;
              if (iVar23 == 0) {
                local_180 = local_180 + lVar26;
                FUN_ffff0000be05dd74(PTR_DAT_ffff0000be0f5570,local_90);
              }
            } while (uVar34 != 0);
            lVar26 = local_1a0 + 0xff;
            if (-1 < local_1a0) {
              lVar26 = local_1a0;
            }
            FUN_ffff0000be0114f0
                      (s____Compress_memory__llx__done__s_ffff0000be0a564e,*local_138,0,lVar26 >> 8)
            ;
          }
          else {
            if (iVar18 != 0) goto LAB_ffff0000be00c990;
            lVar26 = *local_138;
            uVar34 = local_138[1];
            bVar14 = uVar34 == 0;
            iVar18 = (uint)bVar14 << 2;
            FUN_ffff0000be05fd98
                      (s__s__d_____Compress_memory__llx__s_ffff0000be0a9fa7,
                       s_kzip_add_file_no_mapped_ffff0000be0c2896,0x2fc,lVar26,uVar34);
            do {
              uVar32 = uVar34;
              if (0xfff < uVar34) {
                uVar32 = 0x1000;
              }
              FUN_ffff0000be0607b0(puVar25,lVar26,uVar32);
              uVar21 = FUN_ffff0000be0553a4(~(uint)*puVar33,puVar25,uVar32 & 0xffffffff);
              uVar34 = uVar34 - uVar32;
              lVar26 = lVar26 + uVar32;
              local_f8 = CONCAT44(local_f8._4_4_,(int)uVar32);
              *(uint *)puVar33 = ~uVar21;
              local_100 = puVar25;
              do {
                while( true ) {
                  local_e0 = CONCAT44(local_e0._4_4_,0x1000);
                  local_e8 = lVar28;
                  FUN_ffff0000be057f18(&local_100,(ulong)bVar14 << 2);
                  uVar21 = 0x1000 - (int)local_e0;
                  if ((int)uVar21 < 1) break;
                  if (((int)param_1[0x388] != 0) &&
                     (iVar22 = FUN_ffff0000be0091e4
                                         (lVar28,lVar28,uVar21,puVar3,puVar4,puVar38,puVar5),
                     iVar22 != 0)) {
                    FUN_ffff0000be05fd98
                              (s__s__d__encrypt_fail_ffff0000be0ad296,
                               s_kzip_add_file_no_mapped_ffff0000be0c2896,0x318);
                  }
                  uVar20 = (*(code *)param_1[0x40e])(param_1[0x40d],lVar28,uVar21);
                  if ((int)uVar20 < 1) {
                    uVar32 = param_1[2];
                  }
                  else {
                    uVar32 = param_1[2] + (ulong)uVar20;
                    param_1[2] = uVar32;
                  }
                  if (uVar32 - *param_1 >> 0x18 != 0) {
                    FUN_ffff0000be011708(uVar32 >> 0x14);
                    uVar32 = param_1[2];
                    *param_1 = uVar32;
                  }
                  if (0xffff < uVar32 - param_1[1]) {
                    FUN_ffff0000be0357ec();
                    param_1[1] = param_1[2];
                  }
                  if (uVar20 != uVar21) goto LAB_ffff0000be00c988;
                  if ((int)local_e0 != 0) goto LAB_ffff0000be00c3c4;
                }
              } while ((int)local_e0 == 0);
LAB_ffff0000be00c3c4:
            } while (uVar34 != 0);
            FUN_ffff0000be0114f0(s____Compress_memory__llx__done__s_ffff0000be0a6929,*local_138,0);
          }
          plVar1 = local_138 + 1;
          local_138 = local_138 + 6;
          uVar36 = *plVar1 + uVar36;
          bVar14 = true;
          if (iVar18 != 0) {
            uVar34 = param_1[2];
            *(uint *)puVar33 = ~(uint)*puVar33;
            param_1[(ulong)uVar10 * 7 + 7] = uVar34 - uVar31;
            param_1[(ulong)uVar10 * 7 + 8] = uVar36;
            FUN_ffff0000be0578dc(&local_100);
            thunk_FUN_ffff0000be09937c(lVar28);
            thunk_FUN_ffff0000be09937c(puVar25);
            return 1;
          }
        } while( true );
      }
      pcVar29 = s__s__malloc_failed__ffff0000be0ab20c;
      pcVar37 = s_kzip_add_file_ffff0000be0c2888;
LAB_ffff0000be00c970:
      FUN_ffff0000be0115fc(pcVar29,pcVar37);
      goto LAB_ffff0000be00c990;
    }
  }
  else {
    FUN_ffff0000be0093a0(param_1);
    FUN_ffff0000be060e5c(param_3);
    lVar26 = FUN_ffff0000be05f1bc();
    if (lVar26 == 0) {
      FUN_ffff0000be05fd98
                (s__s__d__No_memory_for_encrypted_f_ffff0000be0ab220,
                 s_put_localheader_ffff0000be0b084f,0xf7);
    }
    else {
      uVar27 = FUN_ffff0000be060e5c(param_3);
      iVar18 = FUN_ffff0000be0091e4
                         (param_3,lVar26,uVar27,param_1 + 0x38b,param_1 + 0x3c8,param_1 + 0x389,
                          param_1 + 0x40c);
      if (iVar18 == 0) {
        uVar27 = FUN_ffff0000be060e5c(param_3);
        FUN_ffff0000be0607b0(lVar28,lVar26,uVar27);
        FUN_ffff0000be0093a0(param_1);
        thunk_FUN_ffff0000be09937c(lVar26);
        goto LAB_ffff0000be00be58;
      }
      FUN_ffff0000be05fd98
                (s__s__d__encrypt_fail_ffff0000be0ad296,s_put_localheader_ffff0000be0b084f,0xfd);
      thunk_FUN_ffff0000be09937c(lVar26);
    }
  }
  FUN_ffff0000be05fd98
            (s__s__d__write_local_header_fail_ffff0000be0a9f87,s_kzip_add_file_ffff0000be0c2888,
             0x48c);
  goto LAB_ffff0000be00c0a4;
LAB_ffff0000be00c988:
  FUN_ffff0000be0115fc(s____Compress_memory__llx__error__s_ffff0000be0a561a,*local_138,uVar34);
LAB_ffff0000be00c990:
  FUN_ffff0000be0578dc(&local_100);
  thunk_FUN_ffff0000be09937c(lVar28);
LAB_ffff0000be00c0a4:
  thunk_FUN_ffff0000be09937c(puVar25);
  return 0;
}


