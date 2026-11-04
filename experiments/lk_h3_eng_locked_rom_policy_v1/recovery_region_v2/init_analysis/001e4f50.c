
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void _ZN7android4initL21do_reboot_to_recoveryERKNS0_16BuiltinArgumentsE
               (undefined8 *param_1,long *param_2)

{
  byte *pbVar1;
  ulong uVar2;
  undefined1 *puVar3;
  long lVar4;
  undefined4 uVar5;
  byte bVar6;
  long lVar7;
  code *pcVar8;
  long lVar9;
  byte *pbVar10;
  undefined8 *puVar11;
  undefined4 *puVar12;
  undefined8 uVar13;
  undefined8 uVar14;
  byte bVar15;
  uint uVar16;
  ulong uVar17;
  ulong uVar18;
  byte *pbVar19;
  long lVar20;
  long lVar21;
  undefined8 uVar22;
  byte bStack_d8;
  undefined1 uStack_d7;
  undefined1 uStack_d6;
  undefined5 uStack_d5;
  undefined1 uStack_d0;
  undefined1 auStack_cf [7];
  undefined8 *puStack_c8;
  byte bStack_c0;
  undefined7 uStack_bf;
  undefined1 uStack_b8;
  undefined4 uStack_b7;
  undefined1 uStack_b3;
  undefined2 uStack_b2;
  undefined1 uStack_b0;
  undefined5 uStack_af;
  undefined1 uStack_aa;
  undefined1 uStack_a9;
  byte *pbStack_a0;
  byte *pbStack_98;
  byte *pbStack_90;
  byte *pbStack_88;
  byte *pbStack_80;
  byte *pbStack_78;
  ulong uStack_70;
  undefined8 uStack_68;
  ulong uStack_60;
  long lStack_58;

  lVar9 = tpidr_el0;
  lStack_58 = *(long *)(lVar9 + 0x28);
  lVar20 = *param_2;
  lVar4 = param_2[1];
  uStack_70 = 0;
  uStack_68 = 0;
  uStack_60 = 0;
  pbStack_88 = (byte *)0x0;
  pbStack_80 = (byte *)0x0;
  pbStack_78 = (byte *)0x0;
  lVar7 = lVar4 - lVar20;
  if (lVar7 == 0) {
    pbVar10 = (byte *)0x0;
code_r0x001e5030:
    lVar20 = (long)pbStack_80 - (long)pbVar10;
    pbStack_a0 = (byte *)0x0;
    pbStack_98 = (byte *)0x0;
    uVar17 = (lVar20 >> 3) * -0x5555555555555555;
    pbStack_90 = (byte *)0x0;
    if (uVar17 == 3) {
code_r0x001e51b0:
      param_1[4] = 0;
      param_1[1] = 0;
      *param_1 = 0;
      param_1[3] = 0;
      param_1[2] = 0;
    }
    else {
      if (uVar17 != 4) {
        if (1 < uVar17) {
          uVar18 = *(ulong *)(pbVar10 + 0x20);
          bVar15 = pbVar10[0x18] & 1;
          uVar17 = (ulong)(pbVar10[0x18] >> 1);
          goto code_r0x001e5174;
        }
        goto code_r0x001e51b0;
      }
      bVar15 = pbVar10[0x30];
      uVar17 = (ulong)(bVar15 >> 1);
      if ((bVar15 & 1) != 0) {
        uVar17 = *(ulong *)(pbVar10 + 0x38);
      }
      if (uVar17 != 8) goto code_r0x001e51b0;
      pbVar19 = pbVar10 + 0x31;
      if ((bVar15 & 1) != 0) {
        pbVar19 = *(byte **)(pbVar10 + 0x40);
      }
      if (*(long *)pbVar19 != 0x6e6f736165722d2d) goto code_r0x001e51b0;
      bVar6 = pbVar10[0x18];
      uVar18 = *(ulong *)(pbVar10 + 0x20);
      bVar15 = bVar6 & 1;
      uVar17 = (ulong)(bVar6 >> 1);
      uVar2 = uVar17;
      if ((bVar6 & 1) != 0) {
        uVar2 = uVar18;
      }
      if (uVar2 != 0x12) {
code_r0x001e5174:
        if (bVar15 != 0) {
          uVar17 = uVar18;
        }
        if (uVar17 == 8) {
          pbVar19 = pbVar10 + 0x19;
          if (bVar15 != 0) {
            pbVar19 = *(byte **)(pbVar10 + 0x28);
          }
          if (*(long *)pbVar19 == 0x766e5f6461622d2d) {
            pbStack_98 = (byte *)_ZNSt3__16vectorINS_12basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEENS4_IS6_EEE24__emplace_back_slow_pathIJRA5_KcEEEPS6_DpOT_
                                           (&pbStack_a0,&UNK_0011a4bd);
            uVar17 = func_0x00373940(2,0);
            if ((uVar17 & 1) != 0) {
              puVar12 = (undefined4 *)func_0x003739c0();
              uVar5 = *puVar12;
              func_0x00373950(&bStack_c0,&UNK_00115c56,0x5a3,2,0,0xffffffff);
              uVar13 = func_0x00373960(&bStack_c0);
              _ZNSt3__124__put_character_sequenceB8nn180000IcNS_11char_traitsIcEEEERNS_13basic_ostreamIT_T0_EES7_PKS4_m
                        (uVar13,&UNK_0011b641,0x3b);
              func_0x00373980(&bStack_c0);
              *puVar12 = uVar5;
            }
            puVar11 = (undefined8 *)func_0x00373920(0x20);
            *(undefined1 *)((long)puVar11 + 0x19) = 0;
            uVar13 = _UNK_0010dec9;
            uStack_b8 = 0x19;
            uStack_b7 = 0;
            uStack_b3 = 0;
            uStack_b2 = 0;
            uStack_b0 = SUB81(puVar11,0);
            uStack_af = (undefined5)((ulong)puVar11 >> 8);
            uStack_aa = (undefined1)((ulong)puVar11 >> 0x30);
            uStack_a9 = (undefined1)((ulong)puVar11 >> 0x38);
            bStack_c0 = 0x21;
            uStack_bf = 0;
            puVar11[1] = CONCAT71(_UNK_0010ded2,UNK_0010ded1);
            *puVar11 = uVar13;
            uVar13 = CONCAT17(UNK_0010ded9,_UNK_0010ded2);
            *(undefined8 *)((long)puVar11 + 0x11) = _UNK_0010deda;
            *(undefined8 *)((long)puVar11 + 9) = uVar13;
            goto code_r0x001e52a0;
          }
        }
        goto code_r0x001e51b0;
      }
      pbVar19 = pbVar10 + 0x19;
      if ((bVar6 & 1) != 0) {
        pbVar19 = *(byte **)(pbVar10 + 0x28);
      }
      if ((*(long *)pbVar19 != 0x6f69737265762d2d || *(long *)(pbVar19 + 8) != 0x74616d73696d5f6e)
          || *(short *)(pbVar19 + 0x10) != 0x6863) goto code_r0x001e5174;
      pbStack_98 = (byte *)_ZNSt3__16vectorINS_12basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEENS4_IS6_EEE24__emplace_back_slow_pathIJRA5_KcEEEPS6_DpOT_
                                     (&pbStack_a0,&UNK_0010fa83);
      uVar17 = func_0x00373940(2,0);
      if ((uVar17 & 1) != 0) {
        puVar12 = (undefined4 *)func_0x003739c0();
        uVar5 = *puVar12;
        func_0x00373950(&bStack_c0,&UNK_00115c56,0x59f,2,0,0xffffffff);
        uVar13 = func_0x00373960(&bStack_c0);
        _ZNSt3__124__put_character_sequenceB8nn180000IcNS_11char_traitsIcEEEERNS_13basic_ostreamIT_T0_EES7_PKS4_m
                  (uVar13,&UNK_0010f173,0x3c);
        func_0x00373980(&bStack_c0);
        *puVar12 = uVar5;
      }
      uStack_aa = 0;
      bStack_c0 = 0x2a;
      uStack_b7 = (undefined4)_UNK_0011a4af;
      uStack_b3 = (undefined1)((uint5)_UNK_0011a4af >> 0x20);
      uStack_bf = (undefined7)_UNK_0011a4a7;
      uStack_b8 = (undefined1)((ulong)_UNK_0011a4a7 >> 0x38);
      uStack_b2 = (undefined2)_UNK_0011a4b4;
      uStack_b0 = (undefined1)((uint3)_UNK_0011a4b4 >> 0x10);
      uStack_af = _UNK_0011a4b7;
code_r0x001e52a0:
      bStack_d8 = 2;
      uStack_d7 = 0x30;
      uStack_d6 = 0;
      func_0x00374140(&bStack_c0,&bStack_d8);
      if ((bStack_d8 & 1) != 0) {
        func_0x00373910(puStack_c8);
      }
      if ((bStack_c0 & 1) != 0) {
        func_0x00373910(CONCAT17(uStack_a9,CONCAT16(uStack_aa,CONCAT51(uStack_af,uStack_b0))));
      }
      if (lVar20 == 0x60) {
        bVar15 = pbVar10[0x30];
        uVar17 = (ulong)(bVar15 >> 1);
        if ((bVar15 & 1) != 0) {
          uVar17 = *(ulong *)(pbVar10 + 0x38);
        }
        if (uVar17 == 8) {
          pbVar19 = pbVar10 + 0x31;
          if ((bVar15 & 1) != 0) {
            pbVar19 = *(byte **)(pbVar10 + 0x40);
          }
          if (*(long *)pbVar19 == 0x6e6f736165722d2d) {
            bVar15 = pbVar10[0x48];
            uStack_d0 = 0;
            auStack_cf = (undefined1  [7])0x0;
            puStack_c8 = (undefined8 *)0x0;
            bStack_d8 = 0;
            uStack_d7 = 0;
            uStack_d6 = 0;
            uStack_d5 = 0;
            uVar17 = (ulong)(bVar15 >> 1);
            if ((bVar15 & 1) != 0) {
              uVar17 = *(ulong *)(pbVar10 + 0x50);
            }
            uVar18 = uVar17 + 9;
            if (0xffffffffffffffef < uVar18) goto code_r0x001e56ac;
            if (uVar18 < 0x17) {
              uVar16 = ((uint)uVar18 & 0xff) << 1;
              bStack_d8 = (byte)uVar16;
            }
            else {
              puStack_c8 = (undefined8 *)func_0x00373920((uVar18 | 0xf) + 1);
              lVar20 = (uVar18 | 0xf) + 2;
              uStack_d0 = (undefined1)uVar18;
              auStack_cf = SUB87(uVar18 >> 8,0);
              uVar16 = (uint)lVar20 & 0xff;
              bStack_d8 = (byte)lVar20;
              uStack_d7 = (undefined1)((ulong)lVar20 >> 8);
              uStack_d6 = (undefined1)((ulong)lVar20 >> 0x10);
              uStack_d5 = (undefined5)((ulong)lVar20 >> 0x18);
            }
            puVar3 = auStack_cf + 1;
            puVar11 = (undefined8 *)((ulong)&bStack_d8 | 1);
            if ((uVar16 & 1) != 0) {
              puVar3 = (undefined1 *)((long)puStack_c8 + 9);
              puVar11 = puStack_c8;
            }
            *puVar11 = _UNK_001119ad;
            *(undefined1 *)(puVar11 + 1) = 0x3d;
            if (uVar17 != 0) {
              pbVar19 = pbVar10 + 0x49;
              if ((bVar15 & 1) != 0) {
                pbVar19 = *(byte **)(pbVar10 + 0x58);
              }
              func_0x003757e0(puVar3,pbVar19,uVar17);
            }
            puVar3[uVar17] = 0;
            puVar11 = (undefined8 *)func_0x00373b20(&bStack_d8,&UNK_0011a461,1);
            uVar22 = puVar11[1];
            uVar14 = *puVar11;
            uVar13 = puVar11[2];
            uStack_b8 = (undefined1)uVar22;
            uStack_b7 = (undefined4)((ulong)uVar22 >> 8);
            uStack_b3 = (undefined1)((ulong)uVar22 >> 0x28);
            uStack_b2 = (undefined2)((ulong)uVar22 >> 0x30);
            bStack_c0 = (byte)uVar14;
            uStack_bf = (undefined7)((ulong)uVar14 >> 8);
            uStack_b0 = (undefined1)uVar13;
            uStack_af = (undefined5)((ulong)uVar13 >> 8);
            uStack_aa = (undefined1)((ulong)uVar13 >> 0x30);
            uStack_a9 = (undefined1)((ulong)uVar13 >> 0x38);
            puVar11[1] = 0;
            puVar11[2] = 0;
            *puVar11 = 0;
            if (pbStack_98 < pbStack_90) {
              *(undefined8 *)(pbStack_98 + 0x10) = uVar13;
              *(undefined8 *)(pbStack_98 + 8) = uVar22;
              *(undefined8 *)pbStack_98 = uVar14;
              uStack_b8 = 0;
              uStack_b7 = 0;
              uStack_b3 = 0;
              uStack_b2 = 0;
              uStack_b0 = 0;
              uStack_af = 0;
              uStack_aa = 0;
              uStack_a9 = 0;
              bStack_c0 = 0;
              uStack_bf = 0;
              pbStack_98 = pbStack_98 + 0x18;
            }
            else {
              pbStack_98 = (byte *)_ZNSt3__16vectorINS_12basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEENS4_IS6_EEE24__emplace_back_slow_pathIJS6_EEEPS6_DpOT_
                                             (&pbStack_a0,&bStack_c0);
              if ((bStack_c0 & 1) != 0) {
                func_0x00373910(CONCAT17(uStack_a9,CONCAT16(uStack_aa,CONCAT51(uStack_af,uStack_b0))
                                        ));
              }
            }
            if ((bStack_d8 & 1) != 0) {
              func_0x00373910(puStack_c8);
            }
          }
        }
      }
      uVar17 = _Z24write_bootloader_messageRKNSt3__16vectorINS_12basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEEENS4_IS6_EEEEPS6_
                         (&pbStack_a0,&uStack_70);
      if (((uVar17 & 1) == 0) && (uVar17 = func_0x00373940(4,0), (uVar17 & 1) != 0)) {
        puVar12 = (undefined4 *)func_0x003739c0();
        uVar5 = *puVar12;
        func_0x00373950(&bStack_c0,&UNK_00115c56,0x5ae,4,0,0xffffffff);
        uVar13 = func_0x00373960(&bStack_c0);
        uVar13 = _ZNSt3__124__put_character_sequenceB8nn180000IcNS_11char_traitsIcEEEERNS_13basic_ostreamIT_T0_EES7_PKS4_m
                           (uVar13,&UNK_00114c27,0x37);
        uVar17 = (ulong)&uStack_70 | 1;
        if ((uStack_70 & 1) != 0) {
          uVar17 = uStack_60;
        }
        uVar14 = func_0x00373a40(uVar17);
        _ZNSt3__124__put_character_sequenceB8nn180000IcNS_11char_traitsIcEEEERNS_13basic_ostreamIT_T0_EES7_PKS4_m
                  (uVar13,uVar17,uVar14);
        func_0x00373980(&bStack_c0);
        *puVar12 = uVar5;
      }
      bStack_c0 = 0x18;
      uStack_b7 = 0x6c746372;
      bStack_d8 = 0x1e;
      uStack_b3 = 0;
      uStack_bf = (undefined7)_UNK_001186c6;
      uStack_b8 = (undefined1)((ulong)_UNK_001186c6 >> 0x38);
      puStack_c8 = (undefined8 *)((ulong)puStack_c8 & 0xffffffffffffff00);
      uStack_d7 = (undefined1)_UNK_001170d3;
      uStack_d6 = (undefined1)((uint7)_UNK_001170d3 >> 8);
      uStack_d5 = (undefined5)((uint7)_UNK_001170d3 >> 0x10);
      uStack_d0 = UNK_001170da;
      auStack_cf = (undefined1  [7])_UNK_001170db;
      func_0x00374140(&bStack_c0,&bStack_d8);
      if ((bStack_d8 & 1) != 0) {
        func_0x00373910(puStack_c8);
      }
      if ((bStack_c0 & 1) != 0) {
        func_0x00373910(CONCAT17(uStack_a9,CONCAT16(uStack_aa,CONCAT51(uStack_af,uStack_b0))));
      }
      pbVar10 = pbStack_a0;
      param_1[4] = 0;
      param_1[1] = 0;
      *param_1 = 0;
      param_1[3] = 0;
      param_1[2] = 0;
      pbVar19 = pbStack_98;
      if (pbStack_a0 != (byte *)0x0) {
        while (pbVar1 = pbVar19, pbVar1 != pbVar10) {
          pbVar19 = pbVar1 + -0x18;
          if ((*pbVar19 & 1) != 0) {
            func_0x00373910(*(undefined8 *)(pbVar1 + -8));
          }
        }
        pbStack_98 = pbVar10;
        func_0x00373910(pbStack_a0);
      }
    }
    pbVar10 = pbStack_88;
    pbVar19 = pbStack_80;
    if (pbStack_88 != (byte *)0x0) {
      while (pbVar1 = pbVar19, pbVar1 != pbVar10) {
        pbVar19 = pbVar1 + -0x18;
        if ((*pbVar19 & 1) != 0) {
          func_0x00373910(*(undefined8 *)(pbVar1 + -8));
        }
      }
      func_0x00373910(pbVar10);
    }
    if ((uStack_70 & 1) != 0) {
      func_0x00373910(uStack_60);
    }
    if (*(long *)(lVar9 + 0x28) == lStack_58) {
      return;
    }
    func_0x00375790();
  }
  else if ((ulong)((lVar7 >> 3) * -0x5555555555555555) < 0xaaaaaaaaaaaaaab) {
    pbVar10 = (byte *)func_0x00373920(lVar7);
    lVar21 = 0;
    pbStack_78 = pbVar10 + lVar7;
    pbStack_88 = pbVar10;
    do {
      pbVar19 = (byte *)(lVar20 + lVar21);
      pbVar1 = pbVar10 + lVar21;
      if ((*pbVar19 & 1) == 0) {
        uVar14 = *(undefined8 *)(pbVar19 + 8);
        uVar13 = *(undefined8 *)pbVar19;
        *(undefined8 *)(pbVar1 + 0x10) = *(undefined8 *)(pbVar19 + 0x10);
        *(undefined8 *)(pbVar1 + 8) = uVar14;
        *(undefined8 *)pbVar1 = uVar13;
      }
      else {
        _ZNSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEE25__init_copy_ctor_externalEPKcm
                  (pbVar1,*(undefined8 *)(pbVar19 + 0x10),*(undefined8 *)(pbVar19 + 8));
      }
      lVar21 = lVar21 + 0x18;
    } while (lVar20 + lVar21 != lVar4);
    pbStack_80 = pbVar10 + lVar21;
    goto code_r0x001e5030;
  }
  _ZNKSt3__16vectorIjNS_9allocatorIjEEE20__throw_length_errorB8nn180000Ev(&pbStack_88);
code_r0x001e56ac:
  _ZNKSt3__112basic_stringIcNS_11char_traitsIcEENS_9allocatorIcEEE20__throw_length_errorB8nn180000Ev
            (&bStack_d8);
                    /* WARNING: Does not return */
  pcVar8 = (code *)UndefinedInstructionException(0,0x1e56b4);
  (*pcVar8)();
}
