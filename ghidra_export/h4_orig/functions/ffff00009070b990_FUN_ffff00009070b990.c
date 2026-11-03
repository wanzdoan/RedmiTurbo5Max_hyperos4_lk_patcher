// ffff00009070b990  size=468  FUN_ffff00009070b990

/* WARNING: Type propagation algorithm not settling */

void FUN_ffff00009070b990(void)

{
  long lVar1;
  int iVar2;
  char *pcVar3;
  long lVar4;
  undefined8 uVar5;
  long lVar6;
  undefined8 local_88;
  undefined8 uStack_80;
  undefined8 local_78;
  undefined8 uStack_70;
  undefined8 local_68;
  undefined8 uStack_60;
  undefined8 local_58;
  undefined8 uStack_50;
  long local_48 [3];
  int local_14;
  
  local_14 = 0;
  local_48[1] = 0;
  local_48[2] = 0;
  local_48[0] = 0;
  FUN_ffff000090781ca0(local_48 + 2,&local_14);
  if ((local_48[2] == 0) || (local_14 == 0)) {
    FUN_ffff00009077e11c(s_failed_to_get_pstore_mem_ffff0000907f261a);
    pcVar3 = s_failed_to_get_pstore_mem_ffff0000907e7437;
  }
  else {
    iVar2 = FUN_ffff00009077ba64
                      (DAT_ffff000090839928,s_lkmsg_ffff0000907f3f3e,local_14 + 0xfffU & 0xfffff000,
                       local_48,0xc,local_48[2] & 0xfffff000,0,0);
    if (iVar2 == 0) {
      FUN_ffff00009077ee50(local_48 + 1,0,8);
      lVar1 = local_48[0];
      lVar4 = FUN_ffff00009077d514(0x200000);
      if (lVar4 != 0) {
        FUN_ffff00009077e11c
                  (s_oem_dump_lkmsg__log_header___0x__ffff0000907d7369,lVar1 + 0xf000,0x200000);
        local_58 = 0;
        uStack_50 = 0;
        local_68 = 0;
        uStack_60 = 0;
        local_78 = 0;
        uStack_70 = 0;
        local_88 = 0;
        uStack_80 = 0;
        FUN_ffff00009077ee50(&local_88,0,0x40);
        FUN_ffff00009077f6fc(&local_88,0x40,s_DATA_08x_ffff0000907f5c7a,0x200000);
        uVar5 = FUN_ffff00009077f250(&local_88);
        FUN_ffff00009070fa54(&local_88,uVar5);
        FUN_ffff00009077eb20(lVar4,lVar1 + 0xf00c,0xff4);
        lVar6 = 0;
        do {
          FUN_ffff00009077eb20(lVar4 + lVar6 + 0xff4,lVar1 + 0x10000 + lVar6,0x1000);
          lVar6 = lVar6 + 0x1000;
        } while (lVar6 != 0x1ff000);
        *(undefined1 *)(lVar4 + 0x1fffff) = 0;
        FUN_ffff00009070fa54(lVar4,0x200000);
        FUN_ffff000090709cdc(&DAT_ffff0000907df338);
        FUN_ffff00009077e11c(s_lkmsg_log______s_ffff0000907d73a1,lVar4);
        thunk_FUN_ffff0000907c314c(lVar4);
        FUN_ffff00009077be30(DAT_ffff000090839928,local_48[0]);
        return;
      }
      pcVar3 = s_alloc_data_buf_fil_ffff0000907f5c83;
    }
    else {
      FUN_ffff00009077e11c(s__d__map_error_for_log_store_ffff0000907f2635);
      pcVar3 = s_map_mem_failed__ffff0000907fcb46;
    }
  }
  FUN_ffff000090709c9c(pcVar3);
  return;
}


