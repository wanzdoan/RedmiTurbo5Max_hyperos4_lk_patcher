// ffff00009073fa2c  size=584  FUN_ffff00009073fa2c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009073fa2c(code *param_1)

{
  long lVar1;
  int iVar2;
  int iVar3;
  long lVar4;
  undefined8 uVar5;
  ulong uVar6;
  long lVar7;
  undefined4 *puVar8;
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
  undefined8 local_70;
  undefined8 uStack_68;
  
  lVar4 = FUN_ffff00009077d514(0x1000);
  if (lVar4 != 0) {
    lVar1 = lVar4 + 0x1000;
    iVar3 = 0x1c00d500;
    puVar8 = (undefined4 *)0xffff00001c00d4fc;
    local_70 = 0;
    uStack_68 = 0;
    local_80 = 0;
    uStack_78 = 0;
    local_90 = 0;
    uStack_88 = 0;
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
    lVar7 = lVar4;
    do {
      puVar8 = puVar8 + 1;
      iVar2 = FUN_ffff00009077f6fc
                        (lVar7,lVar1 - lVar7,s_SPM_regs_0x_x____0x_08x_ffff0000907eaf7b,iVar3,
                         *puVar8);
      iVar3 = iVar3 + 4;
      lVar7 = lVar7 + iVar2;
    } while (puVar8 < (undefined4 *)0xffff00001c00d595);
    iVar3 = FUN_ffff00009077f6fc
                      (lVar7,lVar1 - lVar7,s_SPM_regs_0x_x____0x_08x_ffff0000907eaf7b,0x1c00d5c4,
                       _DAT_ffff00001c00d5c4);
    lVar7 = lVar7 + iVar3;
    iVar3 = FUN_ffff00009077f6fc
                      (lVar7,lVar1 - lVar7,s_SPM_regs_0x_x____0x_08x_ffff0000907eaf7b,0x1c00d5c8,
                       _DAT_ffff00001c00d5c8);
    lVar7 = lVar7 + iVar3;
    iVar3 = FUN_ffff00009077f6fc
                      (lVar7,lVar1 - lVar7,s_SPM_regs_0x_x____0x_08x_ffff0000907eaf7b,0x1c00d5cc,
                       _DAT_ffff00001c00d5cc);
    lVar7 = lVar7 + iVar3;
    iVar3 = FUN_ffff00009077f6fc
                      (lVar7,lVar1 - lVar7,s_SPM_regs_0x_x____0x_08x_ffff0000907eaf7b,0x1c00d5d0,
                       _DAT_ffff00001c00d5d0);
    lVar7 = lVar7 + iVar3;
    iVar3 = FUN_ffff00009077f6fc
                      (lVar7,lVar1 - lVar7,s_SPM_regs_0x_x____0x_08x_ffff0000907eaf7b,0x1c00d5d4,
                       _DAT_ffff00001c00d5d4);
    lVar7 = lVar7 + iVar3;
    iVar2 = FUN_ffff00009077f6fc
                      (lVar7,lVar1 - lVar7,s_SPM_regs_0x_x____0x_08x_ffff0000907eaf7b,0x1c00d5d8,
                       _DAT_ffff00001c00d5d8);
    iVar3 = 0x1c00d630;
    lVar7 = lVar7 + iVar2;
    puVar8 = (undefined4 *)0xffff00001c00d62c;
    do {
      puVar8 = puVar8 + 1;
      iVar2 = FUN_ffff00009077f6fc
                        (lVar7,lVar1 - lVar7,s_SPM_regs_0x_x____0x_08x_ffff0000907eaf7b,iVar3,
                         *puVar8);
      iVar3 = iVar3 + 4;
      lVar7 = lVar7 + iVar2;
    } while (puVar8 < (undefined4 *)0xffff00001c00d6cd);
    iVar3 = FUN_ffff000090740028(&local_e0,0x80);
    if (-1 < iVar3) {
      iVar3 = FUN_ffff00009077f6fc
                        (lVar7,lVar1 - lVar7,s_SPM_firmware_version____s_ffff0000907d940f,&local_e0)
      ;
      lVar7 = lVar7 + iVar3;
    }
    uVar5 = FUN_ffff00009077b334(lVar4);
    uVar6 = (*param_1)(uVar5,lVar7 - lVar4);
    if ((uVar6 & 1) == 0) {
      FUN_ffff00009077e11c(s__spm__AEE_EXPDB_dump_fail_ffff0000907fbb6f);
    }
    thunk_FUN_ffff0000907c314c(lVar4);
    return;
  }
  FUN_ffff00009077e11c(s__spm__can_not_alloc_buffer_ffff0000907fd6c7);
  return;
}


