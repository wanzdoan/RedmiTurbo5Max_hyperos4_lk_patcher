
ulong FUN_ffff000090743164(long param_1,long param_2,uint param_3,uint param_4)

{
  int iVar1;
  uint uVar2;
  long lVar3;
  long lVar4;
  char *pcVar5;
  uint uVar6;
  ulong uVar7;
  undefined4 uVar8;
  uint local_dc;
  long local_d0;
  undefined8 local_c8;
  undefined8 local_c0;
  long local_b8;
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
  
  FUN_ffff00009077e11c(s__VCP___s_ffff0000907f4bf3,s_vcp_zip_compress_ffff0000907dca4f);
  lVar3 = FUN_ffff00009077d514(0x10000);
  if (lVar3 == 0) {
    pcVar5 = s__VCP__ibuf_malloc_fail_ffff0000907d4862;
  }
  else {
    lVar4 = FUN_ffff00009077d514(0x10000);
    if (lVar4 != 0) {
      local_70 = 0;
      uStack_68 = 0;
      local_a0 = 0;
      uStack_98 = 0;
      local_b0 = 0;
      uStack_a8 = 0;
      local_c0 = 0;
      local_b8 = 0;
      local_d0 = 0;
      local_c8 = 0;
      local_80 = 0;
      uStack_78 = 0;
      local_90 = 0;
      uStack_88 = 0;
      FUN_ffff00009077ee50(&local_d0,0,0x70);
      uVar2 = FUN_ffff000090772920(&local_d0,9,8,0x1f,8,0,s_1_3_1_1_motley_ffff0000907ebf53,0x70);
      if (uVar2 != 0) {
        FUN_ffff00009077e11c(s__VCP__ret____d_ffff000090802998,(ulong)uVar2);
        thunk_FUN_ffff0000907c314c(lVar3);
        thunk_FUN_ffff0000907c314c(lVar4);
        return (ulong)uVar2;
      }
      local_dc = 0;
      uVar7 = 0;
      do {
        uVar2 = param_3 >> 0x10;
        local_c8 = CONCAT44(local_c8._4_4_,0x10000);
        local_b0 = CONCAT44(local_b0._4_4_,0x10000);
        if (uVar2 == 0) {
          uVar8 = 4;
          local_c8 = CONCAT44(local_c8._4_4_,param_3);
          uVar6 = param_3;
        }
        else {
          uVar8 = 0;
          uVar6 = 0x10000;
        }
        local_d0 = lVar3;
        local_b8 = lVar4;
        FUN_ffff00009077eb20(lVar3,param_1 + (ulong)local_dc,uVar6);
        iVar1 = (int)local_c8;
        local_dc = (int)local_c8 + local_dc;
        local_d0 = lVar3;
        do {
          local_b0 = CONCAT44(local_b0._4_4_,0x10000);
          local_b8 = lVar4;
          FUN_ffff000090773214(&local_d0,uVar8);
          uVar6 = (0x10000 - (int)local_b0) + (int)uVar7;
          if (uVar6 < param_4) {
            FUN_ffff00009077eb20(param_2 + uVar7,lVar4);
            uVar7 = (ulong)uVar6;
          }
          else {
            FUN_ffff00009077e11c(s__VCP__zip_buffer_overflow___ffff0000907fd7a8);
          }
          FUN_ffff000090746154();
        } while ((int)local_b0 == 0);
        param_3 = param_3 - iVar1;
      } while (uVar2 != 0);
      thunk_FUN_ffff0000907c314c(lVar3);
      thunk_FUN_ffff0000907c314c(lVar4);
      FUN_ffff000090772bd8(&local_d0);
      return uVar7;
    }
    thunk_FUN_ffff0000907c314c(lVar3);
    pcVar5 = s__VCP__obuf_malloc_fail_ffff0000907f16fa;
  }
  FUN_ffff00009077e11c(pcVar5);
  return 0xfffffffb;
}


