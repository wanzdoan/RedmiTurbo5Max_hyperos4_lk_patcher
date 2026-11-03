// ffff000090722d2c  size=420  FUN_ffff000090722d2c

void FUN_ffff000090722d2c(code *param_1)

{
  uint uVar1;
  int iVar2;
  undefined4 *puVar3;
  undefined4 *puVar4;
  undefined8 uVar5;
  ulong uVar6;
  
  puVar3 = (undefined4 *)FUN_ffff00009077d514(0xf0000);
  if (puVar3 != (undefined4 *)0x0) {
    uVar1 = FUN_ffff000090785d18(&DAT_ffff0000907d423c,s_dpmpt_ffff0000907fcfaf,puVar3,0xf0000);
    if ((int)uVar1 < 0) {
      FUN_ffff00009077e11c(s__DPM__load_DPM_image_fail__s_ffff0000907f97b4,s_dpmpt_ffff0000907fcfaf)
      ;
    }
    else if (uVar1 < 0xefe01) {
      puVar4 = (undefined4 *)FUN_ffff00009077d514(0x200);
      if (puVar4 == (undefined4 *)0x0) {
        FUN_ffff00009077e11c(s_p_hdr_malloc_failed_ffff0000907f97d2);
      }
      else {
        FUN_ffff00009077ee50(puVar4,0,0x200);
        puVar4[0xc] = 0x58891689;
        *puVar4 = 0x58881688;
        puVar4[1] = uVar1;
        *(undefined8 *)(puVar4 + 0xd) = 0x100000200;
        puVar4[0x11] = 0x10;
        iVar2 = FUN_ffff00009077f6fc(puVar4 + 2,0x20,&DAT_ffff0000907dbf7c,s_dpmpt_ffff0000907fcfaf)
        ;
        if (iVar2 < 0) {
          FUN_ffff00009077e11c(s_Fail_to_save_DPMPT_name___d__ffff0000907e453e,iVar2);
          thunk_FUN_ffff0000907c314c(puVar3);
          puVar3 = puVar4;
        }
        else {
          uVar5 = FUN_ffff00009077b334(puVar4);
          uVar6 = (*param_1)(uVar5,0x200);
          if ((uVar6 & 1) == 0) {
            FUN_ffff00009077e11c(s_DPM_xfile_hdr_dump_failed__ffff0000907df738);
          }
          uVar5 = FUN_ffff00009077b334(puVar3);
          uVar6 = (*param_1)(uVar5,0xf0000);
          if ((uVar6 & 1) == 0) {
            FUN_ffff00009077e11c(s_DPM_xfile_dump_failed__ffff0000907d9038);
          }
          thunk_FUN_ffff0000907c314c(puVar4);
        }
      }
    }
    else {
      FUN_ffff00009077e11c
                (s_DPMPT_LENGTH_0x_x__is_not_enough_ffff0000908007e1,0xf0000,(ulong)uVar1 + 0x200);
    }
    thunk_FUN_ffff0000907c314c(puVar3);
    return;
  }
  FUN_ffff00009077e11c(s_xfile_ptr_malloc_failed_ffff0000907ec2c7);
  return;
}


