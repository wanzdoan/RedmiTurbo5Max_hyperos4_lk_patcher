// ffff00009070bb64  size=448  FUN_ffff00009070bb64

void FUN_ffff00009070bb64(void)

{
  long lVar1;
  int iVar2;
  char *pcVar3;
  long lVar4;
  undefined8 uVar5;
  long lVar6;
  undefined8 local_80;
  undefined8 uStack_78;
  undefined8 local_70;
  undefined8 uStack_68;
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  long local_40;
  ulong local_38;
  undefined8 local_18;
  
  local_18 = 0;
  local_40 = 0;
  local_38 = 0;
  FUN_ffff000090781ca0(&local_38,(long)&local_18 + 4);
  if ((local_38 == 0) || (local_18._4_4_ == 0)) {
    FUN_ffff00009077e11c(s_failed_to_get_pstore_mem_ffff0000907f261a);
    pcVar3 = s_failed_to_get_pstore_mem_ffff0000907e7437;
  }
  else {
    iVar2 = FUN_ffff00009077ba64
                      (DAT_ffff000090839928,&DAT_ffff0000907edabb,
                       local_18._4_4_ + 0xfffU & 0xfffff000,&local_40,0xc,local_38 & 0xfffff000,0,0)
    ;
    if (iVar2 == 0) {
      FUN_ffff00009077ee50(&local_18,0,4);
      lVar1 = local_40;
      lVar4 = FUN_ffff00009077d514(0x200000);
      if (lVar4 != 0) {
        FUN_ffff00009077e11c
                  (s_oem_dump_lpmsg__log_header___0x__ffff0000907cf113,lVar1 + 0x20f000,0x200000);
        local_50 = 0;
        uStack_48 = 0;
        local_60 = 0;
        uStack_58 = 0;
        local_70 = 0;
        uStack_68 = 0;
        local_80 = 0;
        uStack_78 = 0;
        FUN_ffff00009077f6fc(&local_80,0x40,s_DATA_08x_ffff0000907f5c7a,0x200000);
        uVar5 = FUN_ffff00009077f250(&local_80);
        FUN_ffff00009070fa54(&local_80,uVar5);
        FUN_ffff00009077eb20(lVar4,lVar1 + 0x20f008,0xff8);
        lVar6 = 0;
        do {
          FUN_ffff00009077eb20(lVar4 + lVar6 + 0xff8,lVar1 + 0x210000 + lVar6,0x1000);
          lVar6 = lVar6 + 0x1000;
        } while (lVar6 != 0x1ff000);
        *(undefined1 *)(lVar4 + 0x1fffff) = 0;
        FUN_ffff00009070fa54(lVar4,0x200000);
        FUN_ffff000090709cdc(&DAT_ffff0000907df338);
        FUN_ffff00009077e11c(s_lpmsg_log______s_ffff0000907d579b,lVar4);
        thunk_FUN_ffff0000907c314c(lVar4);
        FUN_ffff00009077be30(DAT_ffff000090839928,local_40);
        return;
      }
      pcVar3 = s_alloc_data_buf_fail_ffff0000907f3f44;
    }
    else {
      FUN_ffff00009077e11c(s__d__map_error_for_log_store_ffff0000907f2635);
      pcVar3 = s_alloc_mem_failed__ffff0000907edac0;
    }
  }
  FUN_ffff000090709c9c(pcVar3);
  return;
}


