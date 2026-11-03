
ulong FUN_ffff00009071f05c(long param_1)

{
  uint uVar1;
  int iVar2;
  int iVar3;
  ulong uVar4;
  uint uVar5;
  int iVar6;
  long lVar7;
  long lVar8;
  int *piVar9;
  int *piVar10;
  int *piVar11;
  int iVar12;
  long lVar13;
  char local_28 [4];
  int local_24;
  
  lVar13 = *(long *)(param_1 + 0x20);
  local_24 = 0;
  local_28[0] = '\0';
  uVar4 = (**(code **)(*(long *)(param_1 + 0x18) + 0x20))(3,0);
  if ((int)uVar4 != 0) {
    iVar6 = *(int *)(lVar13 + 8);
    iVar3 = *(int *)(lVar13 + 0x14);
    (**(code **)(*(long *)(param_1 + 0x18) + 0x10))(local_28,&local_24);
    local_24 = local_24 / 10;
    lVar13 = *(long *)(param_1 + 0x20);
    iVar12 = *(int *)(lVar13 + 0x20);
    iVar3 = (local_24 * (iVar3 + iVar6)) / 10000;
    iVar6 = -iVar3;
    if (local_28[0] == '\0') {
      iVar6 = iVar3;
    }
    iVar6 = (int)uVar4 + iVar6;
    if (*(int *)(lVar13 + 0x28) == 0) {
      uVar5 = iVar6 - 0xaf0U;
      if (iVar6 - 0xaf0U == 0 || iVar6 < 0xaf0) {
        uVar5 = 0xaf0 - iVar6;
      }
    }
    else {
      iVar2 = -iVar3;
      if (local_28[0] == '\0') {
        iVar2 = iVar3;
      }
      iVar3 = (**(code **)(*(long *)(param_1 + 0x18) + 0x20))(0xe,0);
      uVar1 = (iVar2 - iVar6) + iVar3;
      uVar5 = -uVar1;
      if (-1 < (int)uVar1) {
        uVar5 = uVar1;
      }
    }
    iVar3 = *(int *)(lVar13 + 0x24);
    if (uVar5 < 2) {
      uVar5 = 1;
    }
    lVar13 = 0;
    if ((long)(int)uVar5 != 0) {
      lVar13 = ((long)iVar12 * (long)iVar6) / (long)(int)uVar5;
    }
    if (iVar3 != 0) {
      lVar7 = lVar13 * iVar3;
      lVar8 = iVar3 - lVar13;
      if (lVar8 < 1) {
        lVar13 = lVar7;
        if (lVar8 < 0) {
          lVar13 = 0;
          if (lVar8 != 0) {
            lVar13 = lVar7 / lVar8;
          }
          lVar13 = -lVar13;
        }
      }
      else {
        lVar13 = 0;
        if (lVar8 != 0) {
          lVar13 = lVar7 / lVar8;
        }
      }
    }
    piVar9 = *(int **)(*(long *)(param_1 + 0x20) + 0x30);
    iVar6 = (int)lVar13;
    if (iVar6 < piVar9[1]) {
      if (piVar9[0x29] < iVar6) {
        iVar3 = piVar9[3];
        piVar10 = piVar9;
        piVar11 = piVar9 + 2;
        iVar12 = piVar9[1];
        if (iVar6 < iVar3) {
          iVar2 = piVar9[5];
          piVar10 = piVar9 + 2;
          piVar11 = piVar9 + 4;
          iVar12 = iVar3;
          iVar3 = iVar2;
          if (iVar6 < iVar2) {
            iVar3 = piVar9[7];
            piVar11 = piVar9 + 6;
            piVar10 = piVar9 + 4;
            iVar12 = iVar2;
            if (iVar6 < iVar3) {
              iVar2 = piVar9[9];
              piVar10 = piVar11;
              piVar11 = piVar9 + 8;
              iVar12 = iVar3;
              iVar3 = iVar2;
              if (iVar6 < iVar2) {
                iVar3 = piVar9[0xb];
                piVar11 = piVar9 + 10;
                piVar10 = piVar9 + 8;
                iVar12 = iVar2;
                if (iVar6 < iVar3) {
                  iVar2 = piVar9[0xd];
                  piVar10 = piVar11;
                  piVar11 = piVar9 + 0xc;
                  iVar12 = iVar3;
                  iVar3 = iVar2;
                  if (iVar6 < iVar2) {
                    iVar3 = piVar9[0xf];
                    piVar11 = piVar9 + 0xe;
                    piVar10 = piVar9 + 0xc;
                    iVar12 = iVar2;
                    if (iVar6 < iVar3) {
                      iVar2 = piVar9[0x11];
                      piVar10 = piVar11;
                      piVar11 = piVar9 + 0x10;
                      iVar12 = iVar3;
                      iVar3 = iVar2;
                      if (iVar6 < iVar2) {
                        iVar3 = piVar9[0x13];
                        piVar11 = piVar9 + 0x12;
                        piVar10 = piVar9 + 0x10;
                        iVar12 = iVar2;
                        if (iVar6 < iVar3) {
                          iVar2 = piVar9[0x15];
                          piVar10 = piVar11;
                          piVar11 = piVar9 + 0x14;
                          iVar12 = iVar3;
                          iVar3 = iVar2;
                          if (iVar6 < iVar2) {
                            iVar3 = piVar9[0x17];
                            piVar11 = piVar9 + 0x16;
                            piVar10 = piVar9 + 0x14;
                            iVar12 = iVar2;
                            if (iVar6 < iVar3) {
                              iVar2 = piVar9[0x19];
                              piVar10 = piVar11;
                              piVar11 = piVar9 + 0x18;
                              iVar12 = iVar3;
                              iVar3 = iVar2;
                              if (iVar6 < iVar2) {
                                iVar3 = piVar9[0x1b];
                                piVar11 = piVar9 + 0x1a;
                                piVar10 = piVar9 + 0x18;
                                iVar12 = iVar2;
                                if (iVar6 < iVar3) {
                                  iVar2 = piVar9[0x1d];
                                  piVar10 = piVar11;
                                  piVar11 = piVar9 + 0x1c;
                                  iVar12 = iVar3;
                                  iVar3 = iVar2;
                                  if (iVar6 < iVar2) {
                                    iVar3 = piVar9[0x1f];
                                    piVar11 = piVar9 + 0x1e;
                                    piVar10 = piVar9 + 0x1c;
                                    iVar12 = iVar2;
                                    if (iVar6 < iVar3) {
                                      iVar2 = piVar9[0x21];
                                      piVar10 = piVar11;
                                      piVar11 = piVar9 + 0x20;
                                      iVar12 = iVar3;
                                      iVar3 = iVar2;
                                      if (iVar6 < iVar2) {
                                        iVar3 = piVar9[0x23];
                                        piVar11 = piVar9 + 0x22;
                                        piVar10 = piVar9 + 0x20;
                                        iVar12 = iVar2;
                                        if (iVar6 < iVar3) {
                                          iVar2 = piVar9[0x25];
                                          piVar10 = piVar11;
                                          piVar11 = piVar9 + 0x24;
                                          iVar12 = iVar3;
                                          iVar3 = iVar2;
                                          if (iVar6 < iVar2) {
                                            iVar3 = piVar9[0x27];
                                            piVar11 = piVar9 + 0x26;
                                            piVar10 = piVar9 + 0x24;
                                            iVar12 = iVar2;
                                            if (iVar6 < iVar3) {
                                              piVar10 = piVar11;
                                              piVar11 = piVar9 + 0x28;
                                              iVar12 = iVar3;
                                              iVar3 = piVar9[0x29];
                                            }
                                          }
                                        }
                                      }
                                    }
                                  }
                                }
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
        uVar5 = 0;
        if (iVar12 - iVar3 != 0) {
          uVar5 = (*piVar11 * (iVar12 - iVar6) + *piVar10 * (iVar6 - iVar3)) / (iVar12 - iVar3);
        }
        uVar4 = (ulong)uVar5;
      }
      else {
        uVar4 = 0x3c;
      }
    }
    else {
      uVar4 = 0xffffffd8;
    }
  }
  return uVar4;
}


