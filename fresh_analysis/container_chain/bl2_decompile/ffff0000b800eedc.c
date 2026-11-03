
uint FUN_ffff0000b800eedc(undefined8 param_1,undefined4 param_2,undefined8 param_3)

{
  int iVar1;
  char cVar2;
  char cVar3;
  int iVar4;
  char *pcVar5;
  uint *puVar6;
  uint uVar7;
  long lVar8;
  int local_44;
  
  iVar4 = FUN_ffff0000b804d5b0(param_3,0x32);
  iVar1 = iVar4 + 1;
  pcVar5 = (char *)FUN_ffff0000b804bccc((long)iVar1);
  local_44 = 0;
  if (pcVar5 == (char *)0x0) {
    FUN_ffff0000b804c33c
              (s___s__malloc_fail__s_len___d_ffff0000b808a7c0,
               s_gauge_fdt_getprop_u32_ffff0000b8097d40,iVar1);
    uVar7 = 0;
  }
  else {
    FUN_ffff0000b804d428(pcVar5,param_3,(long)iVar1);
    pcVar5[iVar4] = '\0';
    cVar2 = *pcVar5;
    if (cVar2 != '\0') {
      lVar8 = 0;
      do {
        if (cVar2 == '_') {
          cVar3 = '-';
        }
        else {
          cVar3 = FUN_ffff0000b804c238();
        }
        cVar2 = (pcVar5 + lVar8)[1];
        pcVar5[lVar8] = cVar3;
      } while ((cVar2 != '\0') && (uVar7 = (uint)lVar8, lVar8 = lVar8 + 1, uVar7 < 0x32));
    }
    puVar6 = (uint *)FUN_ffff0000b804803c(param_1,param_2,pcVar5,&local_44);
    if ((local_44 < 1) || (puVar6 == (uint *)0x0)) {
      puVar6 = (uint *)FUN_ffff0000b804803c(param_1,param_2,param_3,&local_44);
      iVar1 = local_44;
      thunk_FUN_ffff0000b8082558(pcVar5);
      if (iVar1 < 1) {
        return 0;
      }
      if (puVar6 == (uint *)0x0) {
        return 0;
      }
      uVar7 = *puVar6;
    }
    else {
      thunk_FUN_ffff0000b8082558(pcVar5);
      uVar7 = *puVar6;
    }
    uVar7 = (uVar7 & 0xff00ff00) >> 8 | (uVar7 & 0xff00ff) << 8;
    uVar7 = uVar7 >> 0x10 | uVar7 << 0x10;
  }
  return uVar7;
}


