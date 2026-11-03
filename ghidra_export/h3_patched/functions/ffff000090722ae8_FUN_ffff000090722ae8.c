// ffff000090722ae8  size=420  FUN_ffff000090722ae8

void FUN_ffff000090722ae8(code *param_1)

{
  uint uVar1;
  int iVar2;
  undefined4 *puVar3;
  undefined4 *puVar4;
  undefined8 uVar5;
  ulong uVar6;
  
  puVar3 = (undefined4 *)FUN_ffff00009077d24c(0xf0000);
  if (puVar3 != (undefined4 *)0x0) {
    uVar1 = FUN_ffff000090785a50(&DAT_ffff0000907d41ff,s_dpmpt_ffff0000907fce07,puVar3,0xf0000);
    if ((int)uVar1 < 0) {
      FUN_ffff00009077de54(s__DPM__load_DPM_image_fail__s_ffff0000907f9618,s_dpmpt_ffff0000907fce07)
      ;
    }
    else if (uVar1 < 0xefe01) {
      puVar4 = (undefined4 *)FUN_ffff00009077d24c(0x200);
      if (puVar4 == (undefined4 *)0x0) {
        FUN_ffff00009077de54(s_p_hdr_malloc_failed_ffff0000907f9636);
      }
      else {
        FUN_ffff00009077eb88(puVar4,0,0x200);
        puVar4[0xc] = 0x58891689;
        *puVar4 = 0x58881688;
        puVar4[1] = uVar1;
        *(undefined8 *)(puVar4 + 0xd) = 0x100000200;
        puVar4[0x11] = 0x10;
        iVar2 = FUN_ffff00009077f434(puVar4 + 2,0x20,&DAT_ffff0000907dbe5a,s_dpmpt_ffff0000907fce07)
        ;
        if (iVar2 < 0) {
          FUN_ffff00009077de54(s_Fail_to_save_DPMPT_name___d__ffff0000907e4423,iVar2);
          thunk_FUN_ffff0000907c2ad8(puVar3);
          puVar3 = puVar4;
        }
        else {
          uVar5 = FUN_ffff00009077b06c(puVar4);
          uVar6 = (*param_1)(uVar5,0x200);
          if ((uVar6 & 1) == 0) {
            FUN_ffff00009077de54(s_DPM_xfile_hdr_dump_failed__ffff0000907df67e);
          }
          uVar5 = FUN_ffff00009077b06c(puVar3);
          uVar6 = (*param_1)(uVar5,0xf0000);
          if ((uVar6 & 1) == 0) {
            FUN_ffff00009077de54(s_DPM_xfile_dump_failed__ffff0000907d8fa1);
          }
          thunk_FUN_ffff0000907c2ad8(puVar4);
        }
      }
    }
    else {
      FUN_ffff00009077de54
                (s_DPMPT_LENGTH_0x_x__is_not_enough_ffff000090800605,0xf0000,(ulong)uVar1 + 0x200);
    }
    thunk_FUN_ffff0000907c2ad8(puVar3);
    return;
  }
  FUN_ffff00009077de54(s_xfile_ptr_malloc_failed_ffff0000907ec12d);
  return;
}


