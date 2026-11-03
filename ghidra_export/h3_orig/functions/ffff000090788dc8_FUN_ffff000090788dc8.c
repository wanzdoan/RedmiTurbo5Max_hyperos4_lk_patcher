// ffff000090788dc8  size=696  FUN_ffff000090788dc8

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined1 * FUN_ffff000090788dc8(void)

{
  byte bVar1;
  byte bVar2;
  char cVar3;
  int iVar4;
  uint uVar5;
  undefined8 uVar6;
  char *pcVar7;
  long lVar8;
  undefined1 *puVar9;
  ulong uVar10;
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
  
  puVar9 = &DAT_ffff000090887300;
  local_58 = 0;
  uStack_50 = 0;
  local_68 = 0;
  uStack_60 = 0;
  if (DAT_ffff000090887300 == '\0') {
    local_44 = 0;
    iVar4 = FUN_ffff0000907ce340(&local_68,0x20);
    if (iVar4 == 0) {
      uVar10 = 0;
      _DAT_ffff000090887508 = 0x2020155;
      _DAT_ffff00009088750c = 0x1001;
      do {
        iVar4 = FUN_ffff000090737364(&local_44);
        if (iVar4 != 0) {
          FUN_ffff00009077de54
                    (s__s_get_rnd_failed__ret__08x_ffff0000907fc4e3,
                     s_mi_gen_unlock_token_ffff0000907eed4f,iVar4);
          goto LAB_ffff000090788ff8;
        }
        FUN_ffff00009077e858(uVar10 - 0xffff6f778af2,&local_44,4);
        uVar10 = uVar10 + 4;
      } while (uVar10 < DAT_ffff00009088750d);
      bVar1 = DAT_ffff00009088750b + DAT_ffff00009088750d;
      uVar10 = (ulong)bVar1;
      (&DAT_ffff00009088750c)[uVar10] = 3;
      DAT_ffff00009088750b = bVar1 + 2;
      uVar6 = FUN_ffff00009077ef88(&local_68);
      FUN_ffff00009077e858(uVar10 - 0xffff6f778af2,&local_68,uVar6);
      cVar3 = FUN_ffff00009077ef88(&local_68);
      (&DAT_ffff00009088750d)[uVar10] = cVar3;
      uVar10 = (ulong)(byte)(DAT_ffff00009088750b + cVar3);
      puVar9 = &DAT_ffff00009088750c + uVar10;
      DAT_ffff00009088750b = DAT_ffff00009088750b + cVar3 + 2;
      *puVar9 = 2;
      (&DAT_ffff00009088750d)[uVar10] = 0;
      uVar5 = thunk_FUN_ffff00009074af94(0xc);
      bVar1 = (&DAT_ffff00009088750d)[uVar10];
      uVar5 = (uVar5 & 0xff00ff00) >> 8 | (uVar5 & 0xff00ff) << 8;
      lVar8 = uVar10 - 0xffff6f778af2;
      DAT_ffff00009088750b = DAT_ffff00009088750b + '\x04';
      *(uint *)(lVar8 + (ulong)bVar1) = uVar5 >> 0x10 | uVar5 << 0x10;
      (&DAT_ffff00009088750d)[uVar10] = bVar1 + 4;
      uVar5 = thunk_FUN_ffff00009074af94(0xd);
      bVar1 = (&DAT_ffff00009088750d)[uVar10];
      uVar5 = (uVar5 & 0xff00ff00) >> 8 | (uVar5 & 0xff00ff) << 8;
      *(uint *)(lVar8 + (ulong)bVar1) = uVar5 >> 0x10 | uVar5 << 0x10;
      DAT_ffff00009088750b = DAT_ffff00009088750b + '\x04';
      (&DAT_ffff00009088750d)[uVar10] = bVar1 + 4;
      uVar5 = thunk_FUN_ffff00009074af94(0xe);
      bVar1 = (&DAT_ffff00009088750d)[uVar10];
      uVar5 = (uVar5 & 0xff00ff00) >> 8 | (uVar5 & 0xff00ff) << 8;
      *(uint *)(lVar8 + (ulong)bVar1) = uVar5 >> 0x10 | uVar5 << 0x10;
      DAT_ffff00009088750b = DAT_ffff00009088750b + '\x04';
      (&DAT_ffff00009088750d)[uVar10] = bVar1 + 4;
      uVar5 = thunk_FUN_ffff00009074af94(0xf);
      bVar1 = (&DAT_ffff00009088750d)[uVar10];
      uVar5 = (uVar5 & 0xff00ff00) >> 8 | (uVar5 & 0xff00ff) << 8;
      local_70 = 0;
      local_80 = 0;
      uStack_78 = 0;
      *(uint *)(lVar8 + (ulong)bVar1) = uVar5 >> 0x10 | uVar5 << 0x10;
      local_90 = 0;
      uStack_88 = 0;
      local_a0 = 0;
      uStack_98 = 0;
      local_b0 = 0;
      uStack_a8 = 0;
      (&DAT_ffff00009088750d)[uVar10] = bVar1 + 4;
      local_c0 = 0;
      uStack_b8 = 0;
      _DAT_ffff000090887508 = CONCAT13(DAT_ffff00009088750b + '\x04',_DAT_ffff000090887508);
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
      iVar4 = FUN_ffff00009079456c(&uStack_108);
      if (iVar4 == 0) {
        lVar8 = FUN_ffff00009076fb44((long)&uStack_f8 + 3,0x81,&local_128);
        if (lVar8 != 0) {
          bVar2 = (&DAT_ffff00009088750d)[uVar10];
          bVar1 = DAT_ffff00009088750b + 0xc;
          *(undefined8 *)(puVar9 + (ulong)bVar2 + 2) = local_128;
          *(undefined4 *)(puVar9 + (ulong)bVar2 + 10) = (undefined4)local_120;
          (&DAT_ffff00009088750d)[uVar10] = bVar2 + 0xc;
          _DAT_ffff000090887508 = CONCAT13(bVar1,_DAT_ffff000090887508);
          FUN_ffff000090789080(bVar1 + 4);
          DAT_ffff0000908874ff = 0;
          return &DAT_ffff000090887300;
        }
        pcVar7 = s_hash_ufsid__failed____ffff0000907ebe50;
      }
      else {
        pcVar7 = s_ufs_id_get_failed____ffff0000907fe980;
      }
      FUN_ffff00009077de54(pcVar7);
    }
    else {
      FUN_ffff00009077de54
                (s__s_can_t_get_device_name__ffff0000907f36d4,s_mi_gen_unlock_token_ffff0000907eed4f
                );
    }
LAB_ffff000090788ff8:
    puVar9 = (undefined1 *)0x0;
  }
  return puVar9;
}


