// ffff000090765b6c  size=508  FUN_ffff000090765b6c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090765b6c(long param_1)

{
  int iVar1;
  long lVar2;
  long lVar3;
  long lVar4;
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
  undefined8 local_60;
  undefined8 uStack_58;
  undefined8 local_50;
  undefined8 uStack_48;
  undefined4 local_40;
  undefined4 uStack_3c;
  undefined1 auStack_38 [4];
  int local_34;
  
  local_40 = 0x8000000;
  uStack_3c = 0;
  if (param_1 != 0) {
    local_50 = 0;
    uStack_48 = 0;
    local_60 = 0;
    uStack_58 = 0;
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
    FUN_ffff00009077eb88(&local_b0,0,0x70);
    iVar1 = FUN_ffff000090772658(&local_b0,9,8,0x1f,8,0,s_1_3_1_1_motley_ffff0000907ebdd0,0x70);
    if (iVar1 == 0) {
      lVar2 = FUN_ffff00009077d24c(0x8000000);
      if (lVar2 == 0) {
        FUN_ffff00009077de54
                  (s__s__d__copy_buff_malloc_0x_x_byt_ffff0000907eb517,
                   s_write_expdb_blackbox_ffff0000907ece41,0x9b0,0x8000000);
      }
      else {
        FUN_ffff00009077eb88(lVar2,0,0x8000000);
        lVar3 = FUN_ffff00009077d24c(0x8000);
        if (lVar3 == 0) {
          FUN_ffff00009077de54(s_copy_buff_malloc_0x_x_bytes_fail_ffff00009080139a,0x8000);
        }
        else {
          FUN_ffff00009077eb88(lVar3,0,0x8000);
          lVar4 = FUN_ffff00009077d24c(0x8000);
          if (lVar4 == 0) {
            FUN_ffff00009077de54(s_copy_buff_malloc_0x_x_bytes_fail_ffff00009080139a,0x8000);
            thunk_FUN_ffff0000907c2ad8(lVar2);
            lVar2 = lVar3;
          }
          else {
            local_34 = 0;
            FUN_ffff00009077eb88(lVar4,0,0x8000);
            do {
              FUN_ffff00009076569c
                        (&local_34,auStack_38,lVar3,lVar4,&local_b0,lVar2,&uStack_3c,&local_40);
            } while (local_34 != 4);
            DAT_ffff000090821930 = ~DAT_ffff000090821930;
            FUN_ffff000090772910(&local_b0);
            FUN_ffff0000907658dc(lVar2,_DAT_ffff00009087cc3c,param_1);
            thunk_FUN_ffff0000907c2ad8(lVar4);
            thunk_FUN_ffff0000907c2ad8(lVar3);
          }
        }
        thunk_FUN_ffff0000907c2ad8(lVar2);
      }
    }
    else {
      FUN_ffff00009077de54(s_deflateInit2_failed_ffff0000907d5736);
    }
    return;
  }
  FUN_ffff00009077de54
            (s__s__d____s___expdb_header_is_NUL_ffff0000907ece0c,
             s_write_expdb_blackbox_ffff0000907ece41,0x998,s_BLACKBOX_ffff0000907d9988);
  return;
}


