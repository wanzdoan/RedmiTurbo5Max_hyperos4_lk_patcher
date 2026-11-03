
/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined1 * FUN_ffff000090789088(void)

{
  byte bVar1;
  byte bVar2;
  byte bVar3;
  char cVar4;
  int iVar5;
  uint uVar6;
  undefined8 uVar7;
  char *pcVar8;
  long lVar9;
  undefined1 *puVar10;
  ulong uVar11;
  undefined8 local_128;
  undefined8 local_120;
  undefined8 uStack_118;
  undefined8 local_110;
  undefined8 uStack_108;
  undefined8 local_100;
  undefined8 uStack_f8;
  undefined8 local_f0;
  undefined8 uStack_e8;
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
  undefined8 local_90;
  undefined8 uStack_88;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined2 local_70;
  undefined8 local_68;
  undefined8 uStack_60;
  undefined8 local_58;
  undefined8 uStack_50;
  undefined4 local_44;
  
  puVar10 = &DAT_ffff000090887398;
  local_58 = 0;
  uStack_50 = 0;
  local_68 = 0;
  uStack_60 = 0;
  if (DAT_ffff000090887398 == '\0') {
    local_44 = 0;
    iVar5 = FUN_ffff0000907ce9b4(&local_68,0x20);
    if (iVar5 == 0) {
      uVar11 = 0;
      DAT_ffff0000908875a0 = 0x2020155;
      _DAT_ffff0000908875a4 = 0x1001;
      do {
        iVar5 = FUN_ffff0000907375fc(&local_44);
        if (iVar5 != 0) {
          FUN_ffff00009077e11c
                    (s__s_get_rnd_failed__ret__08x_ffff0000907fc67f,
                     s_mi_gen_unlock_token_ffff0000907eeec0,iVar5);
          goto LAB_ffff0000907892b8;
        }
        FUN_ffff00009077eb20(&DAT_ffff0000908875a6 + uVar11,&local_44,4);
        uVar11 = uVar11 + 4;
      } while (uVar11 < DAT_ffff0000908875a5);
      bVar1 = DAT_ffff0000908875a0._3_1_ + DAT_ffff0000908875a5;
      (&DAT_ffff0000908875a4)[bVar1] = 3;
      DAT_ffff0000908875a0._3_1_ = bVar1 + 2;
      uVar7 = FUN_ffff00009077f250(&local_68);
      FUN_ffff00009077eb20(&DAT_ffff0000908875a6 + bVar1,&local_68,uVar7);
      cVar4 = FUN_ffff00009077f250(&local_68);
      (&DAT_ffff0000908875a5)[bVar1] = cVar4;
      bVar1 = DAT_ffff0000908875a0._3_1_ + cVar4;
      puVar10 = &DAT_ffff0000908875a4 + bVar1;
      DAT_ffff0000908875a0._3_1_ = bVar1 + 2;
      *puVar10 = 2;
      (&DAT_ffff0000908875a5)[bVar1] = 0;
      uVar6 = thunk_FUN_ffff00009074b22c(0xc);
      bVar2 = (&DAT_ffff0000908875a5)[bVar1];
      uVar6 = (uVar6 & 0xff00ff00) >> 8 | (uVar6 & 0xff00ff) << 8;
      DAT_ffff0000908875a0._3_1_ = DAT_ffff0000908875a0._3_1_ + '\x04';
      *(uint *)(&DAT_ffff0000908875a6 + (ulong)bVar2 + (ulong)bVar1) = uVar6 >> 0x10 | uVar6 << 0x10
      ;
      (&DAT_ffff0000908875a5)[bVar1] = bVar2 + 4;
      uVar6 = thunk_FUN_ffff00009074b22c(0xd);
      bVar2 = (&DAT_ffff0000908875a5)[bVar1];
      uVar6 = (uVar6 & 0xff00ff00) >> 8 | (uVar6 & 0xff00ff) << 8;
      *(uint *)(&DAT_ffff0000908875a6 + (ulong)bVar2 + (ulong)bVar1) = uVar6 >> 0x10 | uVar6 << 0x10
      ;
      DAT_ffff0000908875a0._3_1_ = DAT_ffff0000908875a0._3_1_ + '\x04';
      (&DAT_ffff0000908875a5)[bVar1] = bVar2 + 4;
      uVar6 = thunk_FUN_ffff00009074b22c(0xe);
      bVar2 = (&DAT_ffff0000908875a5)[bVar1];
      uVar6 = (uVar6 & 0xff00ff00) >> 8 | (uVar6 & 0xff00ff) << 8;
      *(uint *)(&DAT_ffff0000908875a6 + (ulong)bVar2 + (ulong)bVar1) = uVar6 >> 0x10 | uVar6 << 0x10
      ;
      DAT_ffff0000908875a0._3_1_ = DAT_ffff0000908875a0._3_1_ + '\x04';
      (&DAT_ffff0000908875a5)[bVar1] = bVar2 + 4;
      uVar6 = thunk_FUN_ffff00009074b22c(0xf);
      bVar2 = (&DAT_ffff0000908875a5)[bVar1];
      uVar6 = (uVar6 & 0xff00ff00) >> 8 | (uVar6 & 0xff00ff) << 8;
      local_70 = 0;
      local_80 = 0;
      uStack_78 = 0;
      *(uint *)(&DAT_ffff0000908875a6 + (ulong)bVar2 + (ulong)bVar1) = uVar6 >> 0x10 | uVar6 << 0x10
      ;
      local_90 = 0;
      uStack_88 = 0;
      local_a0 = 0;
      uStack_98 = 0;
      local_b0 = 0;
      uStack_a8 = 0;
      (&DAT_ffff0000908875a5)[bVar1] = bVar2 + 4;
      local_c0 = 0;
      uStack_b8 = 0;
      DAT_ffff0000908875a0 =
           CONCAT13(DAT_ffff0000908875a0._3_1_ + '\x04',(undefined3)DAT_ffff0000908875a0);
      local_d0 = 0;
      uStack_c8 = 0;
      local_e0 = 0;
      uStack_d8 = 0;
      local_f0 = 0;
      uStack_e8 = 0;
      local_100 = 0;
      uStack_f8 = 0;
      local_110 = 0;
      uStack_108 = 0;
      local_120 = 0;
      uStack_118 = 0;
      local_128 = 0;
      iVar5 = FUN_ffff00009079482c(&uStack_108);
      if (iVar5 == 0) {
        lVar9 = FUN_ffff00009076fe0c((long)&uStack_f8 + 3,0x81,&local_128);
        if (lVar9 != 0) {
          bVar3 = (&DAT_ffff0000908875a5)[bVar1];
          bVar2 = DAT_ffff0000908875a0._3_1_ + 0xc;
          *(undefined8 *)(puVar10 + (ulong)bVar3 + 2) = local_128;
          *(undefined4 *)(puVar10 + (ulong)bVar3 + 10) = (undefined4)local_120;
          (&DAT_ffff0000908875a5)[bVar1] = bVar3 + 0xc;
          DAT_ffff0000908875a0 = CONCAT13(bVar2,(undefined3)DAT_ffff0000908875a0);
          FUN_ffff000090789340(bVar2 + 4);
          DAT_ffff000090887597 = 0;
          return &DAT_ffff000090887398;
        }
        pcVar8 = s_hash_ufsid__failed____ffff0000907ebfd3;
      }
      else {
        pcVar8 = s_ufs_id_get_failed____ffff0000907feb28;
      }
      FUN_ffff00009077e11c(pcVar8);
    }
    else {
      FUN_ffff00009077e11c
                (s__s_can_t_get_device_name__ffff0000907f3860,s_mi_gen_unlock_token_ffff0000907eeec0
                );
    }
LAB_ffff0000907892b8:
    puVar10 = (undefined1 *)0x0;
  }
  return puVar10;
}


