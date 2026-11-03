
void FUN_ffff0000be0206a4(code *param_1)

{
  uint uVar1;
  int iVar2;
  undefined4 *puVar3;
  undefined4 *puVar4;
  undefined8 uVar5;
  ulong uVar6;
  
  puVar3 = (undefined4 *)FUN_ffff0000be05f1bc(0xf0000);
  if (puVar3 != (undefined4 *)0x0) {
    uVar1 = FUN_ffff0000be066394(&DAT_ffff0000be0a6c3e,s_dpmpt_ffff0000be0c2b08,puVar3,0xf0000);
    if ((int)uVar1 < 0) {
      FUN_ffff0000be05fd98(s__DPM__load_DPM_image_fail__s_ffff0000be0c06c3,s_dpmpt_ffff0000be0c2b08)
      ;
    }
    else if (uVar1 < 0xefe01) {
      puVar4 = (undefined4 *)FUN_ffff0000be05f1bc(0x200);
      if (puVar4 == (undefined4 *)0x0) {
        FUN_ffff0000be05fd98(s_p_hdr_malloc_failed_ffff0000be0c06e1);
      }
      else {
        FUN_ffff0000be060ae0(puVar4,0,0x200);
        puVar4[0xc] = 0x58891689;
        *puVar4 = 0x58881688;
        puVar4[1] = uVar1;
        *(undefined8 *)(puVar4 + 0xd) = 0x100000200;
        puVar4[0x11] = 0x10;
        iVar2 = FUN_ffff0000be0611dc(puVar4 + 2,0x20,&DAT_ffff0000be0ac42b,s_dpmpt_ffff0000be0c2b08)
        ;
        if (iVar2 < 0) {
          FUN_ffff0000be05fd98(s_Fail_to_save_DPMPT_name___d__ffff0000be0b1beb,iVar2);
          thunk_FUN_ffff0000be09937c(puVar3);
          puVar3 = puVar4;
        }
        else {
          uVar5 = FUN_ffff0000be05d228(puVar4);
          uVar6 = (*param_1)(uVar5,0x200);
          if ((uVar6 & 1) == 0) {
            FUN_ffff0000be05fd98(s_DPM_xfile_hdr_dump_failed__ffff0000be0ae6df);
          }
          uVar5 = FUN_ffff0000be05d228(puVar3);
          uVar6 = (*param_1)(uVar5,0xf0000);
          if ((uVar6 & 1) == 0) {
            FUN_ffff0000be05fd98(s_DPM_xfile_dump_failed__ffff0000be0aa2f7);
          }
          thunk_FUN_ffff0000be09937c(puVar4);
        }
      }
    }
    else {
      FUN_ffff0000be05fd98
                (s_DPMPT_LENGTH_0x_x__is_not_enough_ffff0000be0c4fe2,0xf0000,(ulong)uVar1 + 0x200);
    }
    thunk_FUN_ffff0000be09937c(puVar3);
    return;
  }
  FUN_ffff0000be05fd98(s_xfile_ptr_malloc_failed_ffff0000be0b7642);
  return;
}


