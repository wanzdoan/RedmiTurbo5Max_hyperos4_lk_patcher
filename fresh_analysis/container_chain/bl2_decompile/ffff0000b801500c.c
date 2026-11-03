
void FUN_ffff0000b801500c(code *param_1)

{
  uint uVar1;
  int iVar2;
  undefined4 *puVar3;
  undefined4 *puVar4;
  undefined8 uVar5;
  ulong uVar6;
  
  puVar3 = (undefined4 *)FUN_ffff0000b804bccc(0xf0000);
  if (puVar3 != (undefined4 *)0x0) {
    uVar1 = FUN_ffff0000b8050eb8(&DAT_ffff0000b8087f4c,s_dpmpt_ffff0000b809e348,puVar3,0xf0000);
    if ((int)uVar1 < 0) {
      FUN_ffff0000b804c33c(s__DPM__load_DPM_image_fail__s_ffff0000b809c350,s_dpmpt_ffff0000b809e348)
      ;
    }
    else if (uVar1 < 0xefe01) {
      puVar4 = (undefined4 *)FUN_ffff0000b804bccc(0x200);
      if (puVar4 == (undefined4 *)0x0) {
        FUN_ffff0000b804c33c(s_p_hdr_malloc_failed_ffff0000b809c36e);
      }
      else {
        FUN_ffff0000b804d070(puVar4,0,0x200);
        puVar4[0xc] = 0x58891689;
        *puVar4 = 0x58881688;
        puVar4[1] = uVar1;
        *(undefined8 *)(puVar4 + 0xd) = 0x100000200;
        puVar4[0x11] = 0x10;
        iVar2 = FUN_ffff0000b804d74c(puVar4 + 2,0x20,&DAT_ffff0000b808c500,s_dpmpt_ffff0000b809e348)
        ;
        if (iVar2 < 0) {
          FUN_ffff0000b804c33c(s_Fail_to_save_DPMPT_name___d__ffff0000b80908ce,iVar2);
          thunk_FUN_ffff0000b8082558(puVar3);
          puVar3 = puVar4;
        }
        else {
          uVar5 = FUN_ffff0000b8049ec8(puVar4);
          uVar6 = (*param_1)(uVar5,0x200);
          if ((uVar6 & 1) == 0) {
            FUN_ffff0000b804c33c(s_DPM_xfile_hdr_dump_failed__ffff0000b808dcca);
          }
          uVar5 = FUN_ffff0000b8049ec8(puVar3);
          uVar6 = (*param_1)(uVar5,0xf0000);
          if ((uVar6 & 1) == 0) {
            FUN_ffff0000b804c33c(s_DPM_xfile_dump_failed__ffff0000b808a879);
          }
          thunk_FUN_ffff0000b8082558(puVar4);
        }
      }
    }
    else {
      FUN_ffff0000b804c33c
                (s_DPMPT_LENGTH_0x_x__is_not_enough_ffff0000b80a014c,0xf0000,(ulong)uVar1 + 0x200);
    }
    thunk_FUN_ffff0000b8082558(puVar3);
    return;
  }
  FUN_ffff0000b804c33c(s_xfile_ptr_malloc_failed_ffff0000b809530e);
  return;
}


